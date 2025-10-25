import dayjs from 'dayjs';
import { IDistinct } from 'app/shared/model/distinct.model';

export interface ICommunity {
  id?: number;
  name?: string | null;
  lat?: number | null;
  lon?: number | null;
  studentsCount?: number | null;
  createDate?: dayjs.Dayjs | null;
  distinct?: IDistinct | null;
}

export const defaultValue: Readonly<ICommunity> = {};
