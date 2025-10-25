import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSchools } from 'app/entities/school/school.reducer';
import { getEntities as getCommunities } from 'app/entities/community/community.reducer';
import { createEntity, getEntity, reset, updateEntity } from './student.reducer';

export const StudentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const schools = useAppSelector(state => state.school.entities);
  const communities = useAppSelector(state => state.community.entities);
  const studentEntity = useAppSelector(state => state.student.entity);
  const loading = useAppSelector(state => state.student.loading);
  const updating = useAppSelector(state => state.student.updating);
  const updateSuccess = useAppSelector(state => state.student.updateSuccess);

  const handleClose = () => {
    navigate(`/student${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSchools({}));
    dispatch(getCommunities({}));
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
    values.birthday = convertDateTimeToServer(values.birthday);
    values.registerDate = convertDateTimeToServer(values.registerDate);
    values.updateDate = convertDateTimeToServer(values.updateDate);
    values.latestContractEndDate = convertDateTimeToServer(values.latestContractEndDate);

    const entity = {
      ...studentEntity,
      ...values,
      school: schools.find(it => it.id.toString() === values.school?.toString()),
      community: communities.find(it => it.id.toString() === values.community?.toString()),
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
          birthday: displayDefaultDateTime(),
          registerDate: displayDefaultDateTime(),
          updateDate: displayDefaultDateTime(),
          latestContractEndDate: displayDefaultDateTime(),
        }
      : {
          ...studentEntity,
          birthday: convertDateTimeFromServer(studentEntity.birthday),
          registerDate: convertDateTimeFromServer(studentEntity.registerDate),
          updateDate: convertDateTimeFromServer(studentEntity.updateDate),
          latestContractEndDate: convertDateTimeFromServer(studentEntity.latestContractEndDate),
          school: studentEntity?.school?.id,
          community: studentEntity?.community?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.student.home.createOrEditLabel" data-cy="StudentCreateUpdateHeading">
            创建或编辑 Student
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="student-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="student-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Gender" id="student-gender" name="gender" data-cy="gender" type="text" />
              <ValidatedField
                label="Birthday"
                id="student-birthday"
                name="birthday"
                data-cy="birthday"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Register Date"
                id="student-registerDate"
                name="registerDate"
                data-cy="registerDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Update Date"
                id="student-updateDate"
                name="updateDate"
                data-cy="updateDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Latest Contract End Date"
                id="student-latestContractEndDate"
                name="latestContractEndDate"
                data-cy="latestContractEndDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Contact Number" id="student-contactNumber" name="contactNumber" data-cy="contactNumber" type="text" />
              <ValidatedField label="Parents Name" id="student-parentsName" name="parentsName" data-cy="parentsName" type="text" />
              <ValidatedField id="student-school" name="school" data-cy="school" label="School" type="select">
                <option value="" key="0" />
                {schools
                  ? schools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="student-community" name="community" data-cy="community" label="Community" type="select">
                <option value="" key="0" />
                {communities
                  ? communities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student" replace color="info">
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

export default StudentUpdate;
