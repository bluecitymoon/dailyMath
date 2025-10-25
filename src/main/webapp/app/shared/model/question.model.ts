import dayjs from 'dayjs';
import { IQuestionCategory } from 'app/shared/model/question-category.model';
import { IQuestionType } from 'app/shared/model/question-type.model';
import { IStudentGrade } from 'app/shared/model/student-grade.model';
import { IQuestionOption } from 'app/shared/model/question-option.model';

export interface IQuestion {
  id?: number;
  points?: number | null;
  description?: string | null;
  solution?: string | null;
  solutionExternalLink?: string | null;
  createDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  createBy?: string | null;
  answer?: string | null;
  level?: number | null;
  createByUserId?: number | null;
  questionCategory?: IQuestionCategory | null;
  type?: IQuestionType | null;
  grade?: IStudentGrade | null;
  options?: IQuestionOption[] | null;
}

export const defaultValue: Readonly<IQuestion> = {};
