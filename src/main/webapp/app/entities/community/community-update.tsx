import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDistincts } from 'app/entities/distinct/distinct.reducer';
import { createEntity, getEntity, reset, updateEntity } from './community.reducer';

export const CommunityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const distincts = useAppSelector(state => state.distinct.entities);
  const communityEntity = useAppSelector(state => state.community.entity);
  const loading = useAppSelector(state => state.community.loading);
  const updating = useAppSelector(state => state.community.updating);
  const updateSuccess = useAppSelector(state => state.community.updateSuccess);

  const handleClose = () => {
    navigate(`/community${location.search}`);
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
    if (values.lat !== undefined && typeof values.lat !== 'number') {
      values.lat = Number(values.lat);
    }
    if (values.lon !== undefined && typeof values.lon !== 'number') {
      values.lon = Number(values.lon);
    }
    if (values.studentsCount !== undefined && typeof values.studentsCount !== 'number') {
      values.studentsCount = Number(values.studentsCount);
    }
    values.createDate = convertDateTimeToServer(values.createDate);

    const entity = {
      ...communityEntity,
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
      ? {
          createDate: displayDefaultDateTime(),
        }
      : {
          ...communityEntity,
          createDate: convertDateTimeFromServer(communityEntity.createDate),
          distinct: communityEntity?.distinct?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.community.home.createOrEditLabel" data-cy="CommunityCreateUpdateHeading">
            创建或编辑小区
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>加载中...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="community-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="名称" id="community-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="纬度" id="community-lat" name="lat" data-cy="lat" type="text" />
              <ValidatedField label="经度" id="community-lon" name="lon" data-cy="lon" type="text" />
              <ValidatedField label="学生数" id="community-studentsCount" name="studentsCount" data-cy="studentsCount" type="text" />
              <ValidatedField
                label="创建日期"
                id="community-createDate"
                name="createDate"
                data-cy="createDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="community-distinct" name="distinct" data-cy="distinct" label="区域" type="select">
                <option value="" key="0" />
                {distincts
                  ? distincts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/community" replace color="info">
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

export default CommunityUpdate;
