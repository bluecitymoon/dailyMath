export interface IStudentGrade {
  id?: number;
  name?: string | null;
  index?: number | null;
  term?: number | null;
}

export const defaultValue: Readonly<IStudentGrade> = {};
