export interface IQuestionGroupItem {
  id: number;
  title?: string | null;
  description?: string | null;
  order?: number | null;
  points?: number | null;
  difficulty?: number | null;
  category?: string | null;
  type?: string | null;
  grade?: string | null;
  tags?: string[] | null;
  isActive?: boolean | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  metadata?: Record<string, any> | null;
}

export const defaultValue: Readonly<IQuestionGroupItem> = {
  id: 0,
  title: null,
  description: null,
  order: null,
  points: null,
  difficulty: null,
  category: null,
  type: null,
  grade: null,
  tags: null,
  isActive: true,
  createdAt: null,
  updatedAt: null,
  metadata: null,
};
