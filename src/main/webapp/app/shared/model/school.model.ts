import { IDistinct } from 'app/shared/model/distinct.model';

export interface ISchool {
  id?: number;
  name?: string | null;
  registeredStudentsCount?: number | null;
  pinyin?: string | null;
  distinct?: IDistinct | null;
}

export const defaultValue: Readonly<ISchool> = {};
