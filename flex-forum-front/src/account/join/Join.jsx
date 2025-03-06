import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../common/api';

function containsStringAndNumber(input) {
    const hasString = /[a-zA-Z]/.test(input);
    const hasNumber = /\d/.test(input);
    return hasString && hasNumber && input.length >= 8;
}
function containsStringNumberAndSpecial(input) {

    const check = containsStringAndNumber(input);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(input);


    return check && hasSpecial;
}
const Join = () => {
    const nav = useNavigate();
    const [id, setId] = useState({ value: '', idMsg: '', status: false });
    const [pw, setPw] = useState({ value: '', confirmValue: '', pwMsg: '', confirmValueMsg: '', status: false, confirm: false });

    const join = async () => {
        if (id.value && id.status && pw.value && pw.confirmValue && pw.status && pw.confirm) {
            try {
                const res = await api.post(`/join`, { id: id.value, password: pw.value });
                nav('/', { state: { message: res.data } })

            } catch (err) {
                alert(err.response.data);
            }
        } else {
            alert('모두 입력해주세요.');
        }
    }

    useEffect(() => {
        if (id.value) {
            containsStringAndNumber(id.value) ?
                setId(i => ({ ...i, status: true, idMsg: '사용가능한 아이디입니다.' }))
                : setId(i => ({ ...i, status: false, idMsg: '8자 이상, 영어, 숫자를 포함해야합니다.' }))
        } else {
            setId({ value: '', idMsg: '', status: false });
        }
    }, [id.value])

    useEffect(() => {
        if (pw.value) {
            containsStringNumberAndSpecial(pw.value) ?
                setPw(p => ({ ...p, status: true, pwMsg: '사용 가능한 비밀번호입니다.', confirm: false }))
                : setPw(p => ({ ...p, status: false, pwMsg: '8자 이상, 영어, 숫자, 특수문자를 포함해야합니다.', confirm: false }));
        } else {
            setPw({ value: '', confirmValue: '', pwMsg: '', confirmValueMsg: '', status: false, confirm: false })
        }
    }, [pw.value, pw.confirmValue]);
    useEffect(() => {
        if (pw.value && pw.confirmValue) {
            pw.value === pw.confirmValue ? setPw(p => ({ ...p, confirm: true, confirmValueMsg: '비밀번호가 일치합니다.' })) : setPw(p => ({ ...p, confirm: false }));
        }
    }, [pw.confirmValue, pw.value])


    return (
        <section className="login-container">
            <div className="login-input-container">
                <form onSubmit={(e)=>{
                    e.preventDefault();
                    join();
                }} className="login-input">
                    <div className="login-input-text" style={{ marginTop: 30 }}>Join</div>
                    <div>사용하실 아이디, 비밀번호를 입력해주세요.</div>
                    {/* <div style={{ fontSize: '0.7em', padding: 10, color: 'red' }}>같은 학교 관계자, 학생이거나, 저의 친구인 경우 <br />회원 가입 후 카카오톡 <b>roseia8482</b>로 문의주세요.</div> */}
                    <div style={{ color: id.status ? 'green' : 'red', fontSize: 12, paddingTop: 7 }}>{id.idMsg}</div>
                    <input autoComplete='username' className="login-id" placeholder="아이디" value={id.value} onChange={(e) => setId({ ...id, value: e.target.value })} />
                    <div style={{ color: pw.status ? 'green' : 'red', fontSize: 12, paddingTop: 7 }}>{pw.pwMsg}</div>
                    <input autoComplete='current-password' className="login-pw" placeholder="비밀번호" type="password" value={pw.value} onChange={(e) => { setPw({ ...pw, value: e.target.value }); }} />
                    <div style={{ color: pw.confirm ? 'green' : 'red', fontSize: 12, paddingTop: 7 }}>{pw.confirmValueMsg}</div>
                    {pw.status ? <input autoComplete='current-password' className='login-pw' placeholder='비밀번호 확인' type='password' value={pw.confirmValue} onChange={(e) => setPw({ ...pw, confirmValue: e.target.value })} /> : <></>}

                    {id.value && id.status && pw.value && pw.confirmValue && pw.status && pw.confirm && <button type='submit' className="join-button">가입 신청하기</button>}
                </form>
            </div>
        </section>
    )
}

export default Join;