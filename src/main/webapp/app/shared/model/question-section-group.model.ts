import { IStudentGrade } from 'app/shared/model/student-grade.model';

export interface IQuestionSectionGroup {
  id?: number;
  title?: string | null;
  baseGroupIds?: string | null;
  grade?: IStudentGrade | null;
  displayOrder?: number | null;
}

export const defaultValue: Readonly<IQuestionSectionGroup> = {};
