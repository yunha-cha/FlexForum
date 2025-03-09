import { useLocation, useNavigate, useOutletContext, useParams } from "react-router-dom";
import { useCallback, useEffect, useRef, useState } from "react";
import s from "./ForumDetail.module.css"
import DOMPurify from 'dompurify';
import { download, formattedDate, getUser, truncateString } from "../../common/functions";
import api from "../../common/api";

import { FaHeart } from "react-icons/fa6";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faHeart }  from "@fortawesome/free-regular-svg-icons";

const ForumDetail = () => {

    const { code } = useParams();
    
    const nav = useNavigate();

    //게시글이나, 댓글을 자신만 제거할 수 있어야하기 때문에 나 자신을 알아야함. - 소크라테스
    const my = getUser();   //나
    
    const [page, setPage] = useState(0);

    const [hasMore, setHasMore] = useState(true);
    const loader = useRef(null);

    const [isSliding, setIsSliding] = useState(false);
    const location = useLocation(); // 현재 경로 추적
    const [isShowAttachment, setIsShowAttachment] = useState(false);
    const [forum, setForum] = useState({});
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState({
        content: "",
        ip_address: "",
    });

    const [commentCounts, setCommentCounts] = useState(0);


    // 댓글 onInput 변화 감지 함수
    const handleTextarea = (e) => {
        e.target.style.height = 'auto';
        e.target.style.height = `${e.target.scrollHeight}px`;

        setNewComment((prev) => ({
            ...prev,
            content: e.target.value
        }));

    }

    const getForumDetail = useCallback(async () => {
        
        const res = await api.get(`/forum/${code}`);
        setForum(res.data);
        console.log(res.data);

    }, [code]);


    const deleteForum = async () => {
        try {
            if (window.confirm("정말 삭제하시겠습니까?")) {
                await api.delete(`/forum/${code}`);
                nav(`/forum`);
            }
        } catch (err) {
            console.log(err, "삭제 실패!!");

        }
    }



    // 댓글 조회 요청
    const getCommentList = useCallback(async () => {
        
        if (!hasMore) return;
        try{
            
            const res = await api.get(`/comment/${code}?page=${page}`);
            res.data.last && setHasMore(false);

            setComments((comments) => [...comments, ...res.data.content] );
            setPage((page) => page+1);

            setCommentCounts(res.data.totalElements);  //    페이징 응답 객체 속성에서 가져옴
            console.log(res);
            
        }catch (err){
            console.log("error: ", err);
        }
        
    }, [code, page, hasMore, comments])


    useEffect(() => {

        console.log("comments: ", comments);

    }, [comments])


    /* 댓글 스크롤링 */
      useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
              if (entry.isIntersecting) {
                getCommentList();
              }
              
            },
            { threshold: 0.8 } // 요소가 모두(100%) 보일 때 콜백함수 실행
          );

          if (loader.current){
            observer.observe(loader.current);
          }
      
          return () => observer.disconnect(); // 완료 되면 옵저버 해제
      }, [getCommentList]);



    const registComment = async () => {
        if (newComment.content.trim() === '') {
            return;
        }
        try {
            await api.post(`/comment/${code}`, newComment);
            setNewComment({
                content: "",
                ip_address: ""
            });
        } catch (err) {
            console.log(err);
        } finally {
            
            const res = await api.get(`/comment/${code}?page=${0}`);
            setComments(res.data.content); // 바보다
        }
    }


    const deleteComment = async (code) => {
        try {
            if (window.confirm("댓글을 삭제하시겠습니까?")) {
                await api.delete(`/comment/${code}`);
            }

        } catch (err) {
            console.log(err, "삭제 실패");

        }
        getCommentList();
    }


    useEffect(() => {
        
        if (code) {
            getForumDetail();
            // getCommentList();
        }
    }, [code, getForumDetail])



    // 경로가 변할 때마다 애니메이션 상태 설정
    useEffect(() => {

        setIsSliding(false);  // 처음엔 슬라이딩이 되지 않도록 설정
        const timeoutId = setTimeout(() => {
            setIsSliding(true); // 0.1초 후에 슬라이딩 시작
        }, 100); // 애니메이션 시작을 100ms 후에 설정 (조정 가능)

        return () => clearTimeout(timeoutId); // 컴포넌트가 unmount될 때 타이머 정리


    }, [location]); // location이 바뀔 때마다 애니메이션 재시작




    // HTML 안전하게 렌더링
    const SafeHTMLComponent = (content) => {

        const cleanHTML = DOMPurify.sanitize(content);
        //위험이 없어진 HTML 태그를 렌더링 한다.
        return <div dangerouslySetInnerHTML={{ __html: cleanHTML }} />;
    }



    /* 게시글 추천하기 */
    const handleForumRecommend = async () => {

            const res = await api.post(`/forum/${code}/recommend`);
            console.log(res);
            

    }


    // 댓글 추천하기
    const handleCommentRecommend = async(commentCode) => {
        try{
            await api.post(`/comment/${commentCode}/recommend`);

        }catch (e) {

            console.log(e);
            
        }

    }



    useEffect(() => {

        // getCommentList() 쓰면 2번 호출

    }, [])



    return <div onClick={() => { isShowAttachment && setIsShowAttachment(false) }} className={`${s.forumDetail} ${isSliding ? `${s.sliding}` : ``}`}>
        <h2 className={s.pageTitle}>자유 게시판</h2>

        <div className={s.container}>
            <h2 className={s.title}>{forum.title}</h2>

            <div className={s.containerHeader}>
                <div>작성자 <b>{forum.userId}</b></div>
                <div style={{ display: "flex" }}>
                    {forum.createAt && formattedDate(forum.createAt)}
                    {forum?.userId === my?.accountId || my?.accountRole === 'ROLE_ADMIN' ?
                        <div className={s.deleteBtn} onClick={() => deleteForum()}>
                            <img alt="delete" width={15} src="/deleteIcon.png" />
                        </div> : <></>
                    }
                    {forum.file?.length > 0 &&
                        <div className={s.attachmentContainer}>
                            <div style={{ cursor: 'pointer', userSelect: 'none'}} onClick={() => setIsShowAttachment(!isShowAttachment)}>첨부파일</div>
                            {
                                isShowAttachment &&
                                <article>
                                    {forum.file?.map((f) => (
                                        <div
                                            key={f.attachmentCode}
                                            className={s.file}
                                            onClick={() => download(f)}
                                        >
                                            <div>{truncateString(f.originalName, 10, true)}</div>
                                        </div>
                                    ))}
                                </article>
                            }
                        </div>
                    }
                </div>
            </div>

            <div className={s.containerContent}>
                <div className={s.content}>{forum.content && SafeHTMLComponent(forum.content)}</div>
            </div>

            <div className={s.containerFooter}>
                <div className={s.recommendIcon}  >
                { forum.isRecommend ? 
                <FaHeart 
                style={{color: "#66a1ff", marginRight: "1em", width: "28px", height: "28px", cursor: "pointer"}} 
                onClick={() => {setForum(p=>({...p,isRecommend:!p.isRecommend})); handleForumRecommend(); }}
                /> 
                : 
                <FontAwesomeIcon icon={faHeart} 
                onClick={() => {setForum(p=>({...p,isRecommend:!p.isRecommend})); handleForumRecommend(); }}
                style={{color: "#66a1ff", marginRight: "1em", width: "30px", height: "30px", cursor: "pointer"}} />

                }    
                </div>
                <div style={{ marginRight: "1em" }}>댓글 <b>{commentCounts}</b></div>
                <div>조회수 <b>{forum?.views ? forum.views+1 : 0}</b></div>
            </div>


            
            <div className={s.containerComment}>

                <div className={s.containerWrite}>
                    <textarea rows="2" placeholder="댓글 작성"
                        name="content"
                        onInput={handleTextarea}
                        value={newComment.content}
                    />
                    <button onClick={() => registComment()}>확인</button>
                </div>


                {/* 댓글 리스트 */}
                {comments?.map((comment, idx) => (
                    <div className={s.comment} key={idx}>
                        <div className={s.commentHeader}>
                            <div style={comment?.user?.id === '관리자' ? { color: 'red' } : {}}>{comment?.user?.userId}</div>
                            <div style={{ marginLeft: "0.5em", marginRight: "0.5em" }}>•</div>
                            <div className={s.commentDate}> {comment.createAt && formattedDate(comment.createAt)}</div>
                            <div className={s.recommendIcon}>
                            {/* { comment.isRecommend ? */}
                            
                                <FaHeart
                                onClick={() => handleCommentRecommend(comment.commentCode)}
                                style={{color: "#66a1ff", marginRight: "1em", width: "18px", height: "18px", cursor: "pointer"}} 
                                />
                                {/* : <FontAwesomeIcon icon={faHeart} 
                                style={{color: "#66a1ff", marginRight: "1em", width: "20px", height: "20px", cursor: "pointer"}} />
                            }     */}
                            </div>


                            {comment?.user?.userCode === my?.accountCode || my?.accountRole === 'ROLE_ADMIN' ?
                                <div className={s.deleteBtn} onClick={() => deleteComment(comment.commentCode)}>
                                    <img alt="delete" width={15} src="/deleteIcon.png" />
                                </div> : <></>
                            }
                        </div>
                        <div className={s.commentContent}>{comment.content}</div>

                    </div>
                ))}

                {hasMore ? <div ref={loader} style={{backgroundColor: "grey", height: "40px", opacity: "0"}} />
                         : <></>}
                

            </div>


        </div>

    </div>

}

export default ForumDetail;