import axios from "axios";
import { jwtDecode } from "jwt-decode";

/**
 * 시간 이쁘게 출력해주는 함수
 * @param {*} createAt 시간 배열
 * @returns n분 전, n시간 전 등..
 */
export const formattedDate = (createAt) => {
    try{
        const today = new Date();
        const createDate = new Date(createAt[0], createAt[1]-1, createAt[2], createAt[3], createAt[4], createAt[5]);

        const diffMs = today - createDate;
        
        const diffInMinutes = Math.floor(diffMs / (1000 * 60));
        const diffInHours = Math.floor(diffMs / (1000 * 60 * 60));
        const diffInDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
            
        if (diffInMinutes < 1) return "방금 전";
        if (diffInMinutes < 60) return `${diffInMinutes}분 전`;
        if (diffInHours < 24) return `${diffInHours}시간 전`;
        if (diffInDays < 1) return `${diffInDays}일 전`;
        
        return `${createAt[0]}.${String(createAt[1]).padStart(2, '0')}.${String(createAt[2]).padStart(2, '0')} ${String(createAt[3]).padStart(2, '0')}:${String(createAt[4]).padStart(2, '0')}`;
    } catch(err){
        return '시간 표시 에러';
    }
}

/**
 * 
 * @param {*} str 자를 문자열
 * @param {*} n 몇 번째 부터 자를지
 * @param {*} showExtenstion 파일이라면 확장자 표시할건지 여부
 * @returns 긴 제목은...jpg
 */
export const truncateString = (str, n, showExtenstion) => {    
    try{
        if(showExtenstion){
            if(str.length > n){
              const extention = str.split('.').pop();
              return str.slice(0,n - extention.length) + "..." + extention;
            }
          }
          if (str.length > n) {
              return str.slice(0, n) + "...";
          }
          return str;
    } catch(err){
        return '문자열 자르기 에러';
    }

  }

  /**
   * 파일 다운로드 함수
   * @param {*} file 파일 객체
   */
export const download = (file) => {
    axios({
        url: file.fileFullPath,
        method: 'GET',
        responseType: 'blob',
        headers: {
            Authorization: localStorage.getItem("token")
        },
    })
        .then((response) => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', file.originalName); // 다운로드할 파일 이름 설정
            document.body.appendChild(link);
            link.click();
            link.remove();
        })
        .catch((error) => {
            window.open(file.fileFullPath, '_blank');
        })
}


//내가 누군지 알려주는 함수
export const getUser = () => {
    try{
        return jwtDecode(localStorage.getItem('token'));
    } catch(err){
        console.error('Error By getUser() function');
        return {id:'null',userRole:'null'};
    }
}