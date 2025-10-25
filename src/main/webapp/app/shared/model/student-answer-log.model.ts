import dayjs from 'dayjs';

export interface IStudentAnswerLog {
  id?: number;
  studentId?: number | null;
  questionId?: number | null;
  answer?: string | null;
  correct?: number | null;
  createDate?: dayjs.Dayjs | null;
  winPoints?: number | null;
}

export const defaultValue: Readonly<IStudentAnswerLog> = {};
