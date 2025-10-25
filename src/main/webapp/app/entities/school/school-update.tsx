import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDistincts } from 'app/entities/distinct/distinct.reducer';
import { createEntity, getEntity, reset, updateEntity } from './school.reducer';

export const SchoolUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const distincts = useAppSelector(state => state.distinct.entities);
  const schoolEntity = useAppSelector(state => state.school.entity);
  const loading = useAppSelector(state => state.school.loading);
  const updating = useAppSelector(state => state.school.updating);
  const updateSuccess = useAppSelector(state => state.school.updateSuccess);

  const handleClose = () => {
    navigate(`/school${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDistincts({}));
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
    if (values.registeredStudentsCount !== undefined && typeof values.registeredStudentsCount !== 'number') {
      values.registeredStudentsCount = Number(values.registeredStudentsCount);
    }

    const entity = {
      ...schoolEntity,
      ...values,
      distinct: distincts.find(it => it.id.toString() === values.distinct?.toString()),
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
          ...schoolEntity,
          distinct: schoolEntity?.distinct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.school.home.createOrEditLabel" data-cy="SchoolCreateUpdateHeading">
            创建或编辑 School
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="school-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="school-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label="Registered Students Count"
                id="school-registeredStudentsCount"
                name="registeredStudentsCount"
                data-cy="registeredStudentsCount"
                type="text"
              />
              <ValidatedField label="Pinyin" id="school-pinyin" name="pinyin" data-cy="pinyin" type="text" />
              <ValidatedField id="school-distinct" name="distinct" data-cy="distinct" label="Distinct" type="select">
                <option value="" key="0" />
                {distincts
                  ? distincts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/school" replace color="info">
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

export default SchoolUpdate;
