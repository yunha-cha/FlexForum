import React, { useEffect, useState, useCallback } from 'react';
import { IoIosArrowDropup } from "react-icons/io";

const GoToTopButton = () => {
    const [isVisible, setIsVisible] = useState(false);


    useEffect(() => {

        // 스크롤 이벤트 핸들러
        const handleScroll = () => {

            setIsVisible(window.scrollY > 300);
        };

        window.addEventListener('scroll', handleScroll);

        return () => {
            console.log("🔴 이벤트 리스너 제거");
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    const handleScrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
        console.log("맨 위로 올라가라");
        
    };

    return (
        <div 
            style={{
                position: 'fixed', 
                bottom: '20px', 
                left: '50%', 
                zIndex: '5', 
                display: isVisible ? 'block' : 'none'
            }}
        >
            <button 
                onClick={handleScrollToTop} 
                style={{ 
                    background: 'none', 
                    border: 'none', 
                    cursor: 'pointer' 
                }}
            >
                <IoIosArrowDropup size={40} color="rgb(127, 176, 255)" />
            </button>
        </div>
    );
};

export default GoToTopButton;
