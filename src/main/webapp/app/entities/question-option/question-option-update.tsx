import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { createEntity, getEntity, reset, updateEntity } from './question-option.reducer';

export const QuestionOptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const questions = useAppSelector(state => state.question.entities);
  const questionOptionEntity = useAppSelector(state => state.questionOption.entity);
  const loading = useAppSelector(state => state.questionOption.loading);
  const updating = useAppSelector(state => state.questionOption.updating);
  const updateSuccess = useAppSelector(state => state.questionOption.updateSuccess);

  const handleClose = () => {
    navigate(`/question-option${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuestions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.type !== undefined && typeof values.type !== 'number') {
      values.type = Number(values.type);
    }

    const entity = {
      ...questionOptionEntity,
      ...values,
      question: questions.find(it => it.id.toString() === values.question?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...questionOptionEntity,
          question: questionOptionEntity?.question?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.questionOption.home.createOrEditLabel" data-cy="QuestionOptionCreateUpdateHeading">
            创建或编辑题目选项
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>加载中...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="question-option-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="名称" id="question-option-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="类型" id="question-option-type" name="type" data-cy="type" type="text" />
              <ValidatedField label="图片地址" id="question-option-imageUrl" name="imageUrl" data-cy="imageUrl" type="text" />
              <ValidatedField id="question-option-question" name="question" data-cy="question" label="题目" type="select">
                <option value="" key="0" />
                {questions
                  ? questions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question-option" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">返回</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; 保存
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default QuestionOptionUpdate;
