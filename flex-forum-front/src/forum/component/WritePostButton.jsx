import React from 'react';
import { Link } from 'react-router-dom';
import s from './WritePostButton.module.css'

const WritePostButton = () => {
    return (
        <Link to={'write'}>
            <div className={s.writePostButton}>
                <button>
                    <b>글쓰기</b>
                </button>
            </div>
        </Link>
    );
};

export default WritePostButton;