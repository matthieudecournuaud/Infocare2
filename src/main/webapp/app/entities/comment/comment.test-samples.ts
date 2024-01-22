import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 29880,
  title: 'trop',
  editedBy: 'dans même si',
  editedAt: dayjs('2024-01-21'),
};

export const sampleWithPartialData: IComment = {
  id: 28291,
  title: 'vers autrement pin-pon',
  type: 'retomber reculer actionnaire',
  description: 'modeler y',
  editedBy: 'zzzz',
  editedAt: dayjs('2024-01-21'),
  attachments: 'candide diététiste',
  responseToCommentId: 6213,
};

export const sampleWithFullData: IComment = {
  id: 27009,
  title: 'échouer hisser',
  type: 'badaboum',
  visibility: 'par suite de broum',
  description: 'débile patientèle',
  editedBy: 'croâ',
  editedAt: dayjs('2024-01-22'),
  attachments: 'limiter',
  responseToCommentId: 31398,
};

export const sampleWithNewData: NewComment = {
  title: 'hi de crainte que',
  editedBy: 'tôt responsable à raison de',
  editedAt: dayjs('2024-01-21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
