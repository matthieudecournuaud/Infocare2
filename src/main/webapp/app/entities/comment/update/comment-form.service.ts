import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IComment, NewComment } from '../comment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IComment for edit and NewCommentFormGroupInput for create.
 */
type CommentFormGroupInput = IComment | PartialWithRequiredKeyOf<NewComment>;

type CommentFormDefaults = Pick<NewComment, 'id'>;

type CommentFormGroupContent = {
  id: FormControl<IComment['id'] | NewComment['id']>;
  title: FormControl<IComment['title']>;
  type: FormControl<IComment['type']>;
  visibility: FormControl<IComment['visibility']>;
  description: FormControl<IComment['description']>;
  editedBy: FormControl<IComment['editedBy']>;
  editedAt: FormControl<IComment['editedAt']>;
  attachments: FormControl<IComment['attachments']>;
  responseToCommentId: FormControl<IComment['responseToCommentId']>;
  ticket: FormControl<IComment['ticket']>;
};

export type CommentFormGroup = FormGroup<CommentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommentFormService {
  createCommentFormGroup(comment: CommentFormGroupInput = { id: null }): CommentFormGroup {
    const commentRawValue = {
      ...this.getFormDefaults(),
      ...comment,
    };
    return new FormGroup<CommentFormGroupContent>({
      id: new FormControl(
        { value: commentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(commentRawValue.title, {
        validators: [Validators.required],
      }),
      type: new FormControl(commentRawValue.type, {
        validators: [Validators.maxLength(50)],
      }),
      visibility: new FormControl(commentRawValue.visibility, {
        validators: [Validators.maxLength(50)],
      }),
      description: new FormControl(commentRawValue.description),
      editedBy: new FormControl(commentRawValue.editedBy, {
        validators: [Validators.required],
      }),
      editedAt: new FormControl(commentRawValue.editedAt, {
        validators: [Validators.required],
      }),
      attachments: new FormControl(commentRawValue.attachments, {
        validators: [Validators.maxLength(5000)],
      }),
      responseToCommentId: new FormControl(commentRawValue.responseToCommentId),
      ticket: new FormControl(commentRawValue.ticket),
    });
  }

  getComment(form: CommentFormGroup): IComment | NewComment {
    return form.getRawValue() as IComment | NewComment;
  }

  resetForm(form: CommentFormGroup, comment: CommentFormGroupInput): void {
    const commentRawValue = { ...this.getFormDefaults(), ...comment };
    form.reset(
      {
        ...commentRawValue,
        id: { value: commentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CommentFormDefaults {
    return {
      id: null,
    };
  }
}
