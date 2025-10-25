import { IQuestionGroupItem } from './question-group-item.model';

export interface IQuestionBaseGroup {
  id?: number;
  title?: string | null;
  questionIds?: string | null; // 存储结构化的 JSON 数据
}

export const defaultValue: Readonly<IQuestionBaseGroup> = {};
