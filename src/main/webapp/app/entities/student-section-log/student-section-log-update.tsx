import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './student-section-log.reducer';

export const StudentSectionLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studentSectionLogEntity = useAppSelector(state => state.studentSectionLog.entity);
  const loading = useAppSelector(state => state.studentSectionLog.loading);
  const updating = useAppSelector(state => state.studentSectionLog.updating);
  const updateSuccess = useAppSelector(state => state.studentSectionLog.updateSuccess);

  const handleClose = () => {
    navigate(`/student-section-log${location.search}`);
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
    if (values.studentId !== undefined && typeof values.studentId !== 'number') {
      values.studentId = Number(values.studentId);
    }
    if (values.sectionId !== undefined && typeof values.sectionId !== 'number') {
      values.sectionId = Number(values.sectionId);
    }
    if (values.totalCount !== undefined && typeof values.totalCount !== 'number') {
      values.totalCount = Number(values.totalCount);
    }
    if (values.finishedCount !== undefined && typeof values.finishedCount !== 'number') {
      values.finishedCount = Number(values.finishedCount);
    }
    if (values.correctRate !== undefined && typeof values.correctRate !== 'number') {
      values.correctRate = Number(values.correctRate);
    }
    values.createDate = convertDateTimeToServer(values.createDate);
    values.updateDate = convertDateTimeToServer(values.updateDate);

    const entity = {
      ...studentSectionLogEntity,
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
      ? {
          createDate: displayDefaultDateTime(),
          updateDate: displayDefaultDateTime(),
        }
      : {
          ...studentSectionLogEntity,
          createDate: convertDateTimeFromServer(studentSectionLogEntity.createDate),
          updateDate: convertDateTimeFromServer(studentSectionLogEntity.updateDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.studentSectionLog.home.createOrEditLabel" data-cy="StudentSectionLogCreateUpdateHeading">
            创建或编辑 Student Section Log
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
                <ValidatedField name="id" required readOnly id="student-section-log-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Student Id" id="student-section-log-studentId" name="studentId" data-cy="studentId" type="text" />
              <ValidatedField label="Section Id" id="student-section-log-sectionId" name="sectionId" data-cy="sectionId" type="text" />
              <ValidatedField label="Total Count" id="student-section-log-totalCount" name="totalCount" data-cy="totalCount" type="text" />
              <ValidatedField
                label="Finished Count"
                id="student-section-log-finishedCount"
                name="finishedCount"
                data-cy="finishedCount"
                type="text"
              />
              <ValidatedField
                label="Correct Rate"
                id="student-section-log-correctRate"
                name="correctRate"
                data-cy="correctRate"
                type="text"
              />
              <ValidatedField
                label="Create Date"
                id="student-section-log-createDate"
                name="createDate"
                data-cy="createDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Update Date"
                id="student-section-log-updateDate"
                name="updateDate"
                data-cy="updateDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student-section-log" replace color="info">
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

export default StudentSectionLogUpdate;
