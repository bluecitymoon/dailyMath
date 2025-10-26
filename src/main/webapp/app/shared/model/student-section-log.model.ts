import dayjs from 'dayjs';

export interface IStudentSectionLog {
  id?: number;
  studentId?: number | null;
  sectionId?: number | null;
  totalCount?: number | null;
  finishedCount?: number | null;
  correctRate?: number | null;
  createDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IStudentSectionLog> = {};
