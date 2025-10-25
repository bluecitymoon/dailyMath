import React from 'react';
import {
  FaGraduationCap,
  FaMap,
  FaUsers,
  FaSchool,
  FaUser,
  FaLayerGroup,
  FaObjectGroup,
  FaBook,
  FaTags,
  FaListCheck,
  FaListUl,
  FaFolderTree,
  FaHeart,
} from 'react-icons/fa6';
import { FaUserCog, FaTachometerAlt, FaCogs, FaTasks, FaBookOpen } from 'react-icons/fa';
import { IconProp } from '@fortawesome/fontawesome-svg-core';

export interface MenuItem {
  id: string;
  title: string;
  icon: React.ComponentType | IconProp;
  to: string;
  description?: string;
}

export interface MenuGroup {
  id: string;
  title: string;
  icon: React.ComponentType | IconProp;
  items: MenuItem[];
}

export const menuGroups: MenuGroup[] = [
  {
    id: 'question-bank',
    title: '题库管理',
    icon: 'question-circle',
    items: [
      {
        id: 'question',
        title: '题库管理',
        icon: FaBook,
        to: '/question',
        description: '管理所有题目内容，题目可以进行基础分组、章节分组、分类、类型、选项管理',
      },
      {
        id: 'question-base-group',
        title: '题目基础分组',
        icon: FaLayerGroup,
        to: '/question-base-group',
        description: '管理题目基础分组，将题目分成组',
      },
      {
        id: 'question-section-group',
        title: '题目章节管理',
        icon: FaFolderTree,
        to: '/question-section-group',
        description: '管理题目章节分组，将题目分成章节',
      },
      {
        id: 'question-category',
        title: '题目分类',
        icon: FaTags,
        to: '/question-category',
        description: '管理题目分类',
      },
      {
        id: 'question-type',
        title: '题目类型',
        icon: FaListCheck,
        to: '/question-type',
        description: '管理题目类型',
      },
      {
        id: 'question-option',
        title: '题目选项',
        icon: FaListUl,
        to: '/question-option',
        description: '管理题目选项',
      },
    ],
  },
  {
    id: 'basic-data',
    title: '基础数据配置',
    icon: 'database',
    items: [
      {
        id: 'student-grade',
        title: '学生年级',
        icon: FaGraduationCap,
        to: '/student-grade',
        description: '管理学生年级信息',
      },
      {
        id: 'distinct',
        title: '区管理',
        icon: FaMap,
        to: '/distinct',
        description: '管理区域信息',
      },
      {
        id: 'community',
        title: '小区列表',
        icon: FaUsers,
        to: '/community',
        description: '管理小区信息',
      },
      {
        id: 'school',
        title: '学校管理',
        icon: FaSchool,
        to: '/school',
        description: '管理学校信息',
      },
      {
        id: 'student',
        title: '学生管理',
        icon: FaUser,
        to: '/student',
        description: '管理学生信息',
      },
    ],
  },
  {
    id: 'admin',
    title: '系统管理',
    icon: 'users-cog',
    items: [
      {
        id: 'user-management',
        title: '用户管理',
        icon: FaUserCog,
        to: '/admin/user-management',
        description: '管理系统用户',
      },
      {
        id: 'metrics',
        title: '资源监控',
        icon: FaTachometerAlt,
        to: '/admin/metrics',
        description: '查看系统资源使用情况',
      },
      {
        id: 'health',
        title: '服务状态',
        icon: 'heart',
        to: '/admin/health',
        description: '查看服务健康状态',
      },
      {
        id: 'configuration',
        title: '配置',
        icon: 'cogs',
        to: '/admin/configuration',
        description: '系统配置管理',
      },
      {
        id: 'logs',
        title: '日志',
        icon: 'tasks',
        to: '/admin/logs',
        description: '查看系统日志',
      },
      {
        id: 'docs',
        title: 'API文档',
        icon: 'book',
        to: '/admin/docs',
        description: '查看API文档',
      },
    ],
  },
];
