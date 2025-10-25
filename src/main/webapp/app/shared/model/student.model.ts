import dayjs from 'dayjs';
import { ISchool } from 'app/shared/model/school.model';
import { ICommunity } from 'app/shared/model/community.model';

export interface IStudent {
  id?: number;
  name?: string | null;
  gender?: string | null;
  birthday?: dayjs.Dayjs | null;
  registerDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  latestContractEndDate?: dayjs.Dayjs | null;
  contactNumber?: string | null;
  parentsName?: string | null;
  school?: ISchool | null;
  community?: ICommunity | null;
}

export const defaultValue: Readonly<IStudent> = {};
