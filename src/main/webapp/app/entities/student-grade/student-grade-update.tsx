import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './student-grade.reducer';

export const StudentGradeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studentGradeEntity = useAppSelector(state => state.studentGrade.entity);
  const loading = useAppSelector(state => state.studentGrade.loading);
  const updating = useAppSelector(state => state.studentGrade.updating);
  const updateSuccess = useAppSelector(state => state.studentGrade.updateSuccess);

  const handleClose = () => {
    navigate(`/student-grade${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.index !== undefined && typeof values.index !== 'number') {
      values.index = Number(values.index);
    }
    if (values.term !== undefined && typeof values.term !== 'number') {
      values.term = Number(values.term);
    }

    const entity = {
      ...studentGradeEntity,
      ...values,
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
          ...studentGradeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.studentGrade.home.createOrEditLabel" data-cy="StudentGradeCreateUpdateHeading">
            创建或编辑 Student Grade
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="student-grade-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Name" id="student-grade-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Index" id="student-grade-index" name="index" data-cy="index" type="text" />
              <ValidatedField label="Term" id="student-grade-term" name="term" data-cy="term" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student-grade" replace color="info">
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

export default StudentGradeUpdate;
