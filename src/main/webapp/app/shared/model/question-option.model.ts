import { IQuestion } from 'app/shared/model/question.model';

export interface IQuestionOption {
  id?: number;
  name?: string | null;
  type?: number | null;
  imageUrl?: string | null;
  isAnswer?: boolean | null;
  question?: IQuestion | null;
}

export const defaultValue: Readonly<IQuestionOption> = {};
