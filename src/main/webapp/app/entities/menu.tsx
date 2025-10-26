import React from 'react';
import { FaAsterisk, FaGraduationCap, FaLayerGroup, FaMap, FaObjectGroup, FaSchool, FaUser, FaUsers } from 'react-icons/fa6';

import Fa6MenuItem from 'app/shared/layout/menus/fa6-menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <Fa6MenuItem icon={FaGraduationCap} to="/student-grade">
        学生年级
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaMap} to="/distinct">
        区管理
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaUsers} to="/community">
        小区列表
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaSchool} to="/school">
        学校管理
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaUser} to="/student">
        学生管理
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaLayerGroup} to="/question-base-group">
        题目基础分组
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaObjectGroup} to="/question-section-group">
        题目章节分组
      </Fa6MenuItem>
      <Fa6MenuItem icon={FaAsterisk} to="/student-answer-log">
        答题记录
      </Fa6MenuItem>
      <MenuItem icon="asterisk" to="/student-section-log">
        Student Section Log
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
