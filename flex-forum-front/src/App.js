import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from './account/login/Login';
import Join from './account/join/Join';
import ForumMain from './forum/main/ForumMain';
import ForumDetail from './forum/detail/ForumDetail';
import ForumCreate from './forum/create/ForumCreate';

function App() {
  return (

    <BrowserRouter>
      <Routes>
        {/* 계정 관련 */}
        <Route path='/' element={<Login/>}/>
        <Route path='/join' element={<Join/>}/>

        {/* 게시판 관련 */}
        <Route path='/forum' element={<ForumMain/>}/>
        <Route path='/forum/write' element={<ForumCreate/>}/>

        <Route path='/forum/:code' element={<ForumDetail/>}/>

      </Routes>
    </BrowserRouter>
  );
}

export default App;
