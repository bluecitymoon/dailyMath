export interface IBaseGroupItem {
  id: number;
  title?: string | null;
  description?: string | null;
  order?: number | null;
  isActive?: boolean | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  metadata?: Record<string, any> | null;
}

export const defaultValue: Readonly<IBaseGroupItem> = {
  id: 0,
  title: null,
  description: null,
  order: null,
  isActive: true,
  createdAt: null,
  updatedAt: null,
  metadata: null,
};
