import { useCallback, useEffect, useRef, useState } from "react";
import s from "./ForumMain.module.css"
import { useNavigate } from "react-router-dom";
import api from "../../common/api";
import { formattedDate } from "../../common/functions";
import GoToTopButton from "../component/GoToTopButton";
import WritePostButton from "../component/WritePostButton";
import { FaComment, FaHeart } from "react-icons/fa";

const ForumMain = () => {
    const nav = useNavigate();

    // virtual dom 진짜 dom 비교 연산
    // 렌더링 조건
    // 1. useState가 바뀔 때 바꿔치기
    // 2. 부모 컴포넌트의 props 바뀌면 자식들도 리렌더링
    // 

    const [forums, setForums] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(1);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true); // 더 불러올 데이터가 있는지 여부
    const loader = useRef(null);

    const [categorys, setCategorys] = useState([]);
    


    //게시글 가져오기
    const getForumList = useCallback(async() => {

        if (!hasMore) return; // 마지막 페이지 res.data.last = true가 되면 hasMore = false / false이면 함수 빠져나옴
        try{
            const res = await api.get(`/forum?page=${page}`);
            res.data.last && setHasMore(false);
            console.log(res.data.content);

            setForums((prev) => [...prev, ...res.data.content]); // 기존 데이터에 새로 가져온 데이터를 추가
            setPage((prev) => prev + 1); // 다음 페이지로 이동

        } catch (err) {
            console.log(err)
        }
    }, [page, hasMore]);

    

    // 카테고리에 따른 게시글들 가져오기
    const handleForums = (categoryCode) => {
        
        console.log("categoryCode: ", categoryCode);
        

        // 불가능, 왜냐하면 페이징을 했기 때문에, 페이징으로 5개씩 가져오기 때문에 set해도 5개밖에 세팅안됨
        // 서버에 계속 페이지를 요청해야 하니까 안된다.
        // if(categoryCode !== null){  
        //     setForums(forums => forums.filter(forum => forum.categoryCode === categoryCode))
        // }

    }



    /* 카테고리 */
    const getCategoryList = async() => {
       const res = await api.get(`/forum/category`);
       setCategorys(res.data);

    }

    useEffect(() => {
        getCategoryList();

    }, [])



    //스크롤 이벤트
    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {

              if (entry.isIntersecting) {
                getForumList();
              }
            },
            { threshold: 0.8 } // 요소가 모두(100%) 보일 때 콜백함수 실행
          );

          if (loader.current){
            observer.observe(loader.current);
          }
      
          return () => observer.disconnect(); // 완료 되면 옵저버 해제
      }, [getForumList]);


    //조회수 증가, 페이지 이동
    const enterForum = (forumCode) => {
        api.post(`/views/${forumCode}`)
        nav(`/forum/${forumCode}`); 
    }

    return <div className={s.container}>
        <GoToTopButton/>
        <WritePostButton/>

        <div className={s.mainContainer}>

        <div className={s.forumContainer}>
        <h2 id="forumTitle">자유 게시판</h2>
        {forums.map((forum, idx) => {

            return(
                <div
                key={forum.forumCode}
                className={s.item}
                onClick={() => {enterForum(forum.forumCode)}}>
                    <div className={s.date}>{formattedDate(forum.createAt)}</div>
                    <div className={s.itemContent}>
                        <h3>{forum.title}</h3>
                        {/* <p>{SafeHTMLComponent(forum.content.length) > 45 ? SafeHTMLComponent(forum.content).slice(0, 45) + '...' : SafeHTMLComponent(forum.content)}</p> */}
                    </div>

                    <div className={s.itemWrap}>
                        <div className={s.writerWrap}>
                            <div>🐹</div>
                            <div className={s.writer}>{forum.userId}</div>
                        </div>
                        <div className={s.detailWrap}>
                        <div style={{display: "flex", gap: "5px"}}>
                            <FaHeart style={{color: "#66a1ff", width: "18px", height: "18px"}} />
                            <div>{forum.recommendCounts}</div>
                        </div>

                        <div style={{display: "flex", gap: "5px"}}>
                            <FaComment style={{color: "#66a1ff", width: "18px", height: "18px"}} />
                            <div>{forum.commentCounts}</div>
                        </div>

                        </div>
                    </div>
                
                </div>
            )
        
        })}

        {hasMore ? (
            // observer와 연결된 loader, true일 때만 나와서 처음에 안나옴
            <div className={s.loader} ref={loader} style={{ height: '40px', backgroundColor: "lightgray" }} />
        ) : (
            // 더 이상 데이터가 없으면 메세지
            <div style={{ height: '30px', color: "rgb(127, 176, 255)"}}>
                마지막 게시글 입니다!🎄
            </div>
        )}

        </div>


        <div className={s.categoryContainer}>
            {categorys.map((category) => 
                <div className={s.category}
                     onClick={() => setSelectedCategory(category.categoryCode)}>
                    {category.name}
                </div>
            )
            }
        </div>

        </div>

    </div>

}

export default ForumMain;