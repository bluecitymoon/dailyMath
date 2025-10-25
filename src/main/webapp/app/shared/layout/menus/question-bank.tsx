import React from 'react';
import { FaBook, FaTags, FaListCheck, FaListUl, FaLayerGroup, FaFolderTree } from 'react-icons/fa6';

import Fa6MenuItem from 'app/shared/layout/menus/fa6-menu-item';
import { NavDropdown } from './menu-components';

const QuestionBankMenu = () => {
  return (
    <NavDropdown
      icon="question-circle"
      name="题库管理"
      id="question-bank-menu"
      data-cy="question-bank"
      style={{ maxHeight: '80vh', overflow: 'auto' }}
    >
      {/* prettier-ignore */}
      <Fa6MenuItem icon={FaBook} to="/question">
        题库管理
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaTags} to="/question-category">
        题目分类
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaListCheck} to="/question-type">
        题目类型
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaListUl} to="/question-option">
        题目选项
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaLayerGroup} to="/question-base-group">
        题目基础分组
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaFolderTree} to="/question-section-group">
        题目章节分组
      </Fa6MenuItem>
    </NavDropdown>
  );
};

export default QuestionBankMenu;
