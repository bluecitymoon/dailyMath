import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, Modal, ModalHeader, ModalBody, ModalFooter, Alert } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TextField from '@mui/material/TextField';
import { IQuestionBaseGroup } from 'app/shared/model/question-base-group.model';
import { IBaseGroupItem } from 'app/shared/model/base-group-item.model';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getStudentGrades } from 'app/entities/student-grade/student-grade.reducer';
import { getEntities as getQuestionBaseGroups } from 'app/entities/question-base-group/question-base-group.reducer';
import { getEntity as getQuestionBaseGroupEntity } from 'app/entities/question-base-group/question-base-group.reducer';
import { createEntity, getEntity, reset, updateEntity } from './question-section-group.reducer';

export const QuestionSectionGroupUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studentGrades = useAppSelector(state => state.studentGrade.entities);
  const questionSectionGroupEntity = useAppSelector(state => state.questionSectionGroup.entity);
  const loading = useAppSelector(state => state.questionSectionGroup.loading);
  const updating = useAppSelector(state => state.questionSectionGroup.updating);
  const updateSuccess = useAppSelector(state => state.questionSectionGroup.updateSuccess);

  // 基础分组相关状态
  const questionBaseGroupList = useAppSelector(state => state.questionBaseGroup.entities);
  const questionBaseGroupEntity = useAppSelector(state => state.questionBaseGroup.entity);
  const questionBaseGroupLoading = useAppSelector(state => state.questionBaseGroup.loading);
  const questionBaseGroupError = useAppSelector(state => state.questionBaseGroup.errorMessage);

  const [baseGroups, setBaseGroups] = useState<IBaseGroupItem[]>([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedBaseGroupId, setSelectedBaseGroupId] = useState<string | null>(null);
  const [showError, setShowError] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleClose = () => {
    navigate(`/question-section-group${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudentGrades({}));
    dispatch(getQuestionBaseGroups({ page: 0, size: 10000, sort: 'id,asc' }));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // 处理 baseGroupIds 的解析和设置
  useEffect(() => {
    if (!isNew && questionSectionGroupEntity?.baseGroupIds) {
      try {
        // 解析 baseGroupIds 中的对象数组
        const parsedBaseGroups = JSON.parse(questionSectionGroupEntity.baseGroupIds);
        if (Array.isArray(parsedBaseGroups)) {
          // 将对象数组转换为完整的基础分组对象用于界面显示
          const baseGroupsData = parsedBaseGroups.map((item, index) => ({
            id: item.id || Number(item),
            title: `基础分组 ${index + 1}`,
            description: null,
            order: index + 1,
            isActive: true,
            createdAt: null,
            updatedAt: null,
            metadata: null,
          }));
          setBaseGroups(baseGroupsData);
        } else {
          setBaseGroups([]);
        }
      } catch (error) {
        // 如果解析失败，尝试按逗号分隔的字符串格式解析
        try {
          const groupIds = questionSectionGroupEntity.baseGroupIds.split(',').map(groupId => parseInt(groupId.trim(), 10));
          const baseGroupsData = groupIds.map((groupId, index) => ({
            id: groupId,
            title: `基础分组 ${index + 1}`,
            description: null,
            order: index + 1,
            isActive: true,
            createdAt: null,
            updatedAt: null,
            metadata: null,
          }));
          setBaseGroups(baseGroupsData);
        } catch (parseError) {
          setBaseGroups([]);
        }
      }
    } else if (isNew) {
      setBaseGroups([]);
    }
  }, [questionSectionGroupEntity, isNew]);

  // 处理基础分组加载成功
  useEffect(() => {
    if (questionBaseGroupEntity && selectedBaseGroupId && !questionBaseGroupLoading) {
      setModalOpen(true);
      setShowError(false);
    }
  }, [questionBaseGroupEntity, selectedBaseGroupId, questionBaseGroupLoading]);

  // 处理基础分组加载失败
  useEffect(() => {
    if (questionBaseGroupError && selectedBaseGroupId && !questionBaseGroupLoading) {
      setShowError(true);
      setErrorMessage(`基础分组 ID ${selectedBaseGroupId} 不存在或查询失败`);
      setModalOpen(false);
    }
  }, [questionBaseGroupError, selectedBaseGroupId, questionBaseGroupLoading]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    // 过滤掉无效的基础分组，只保留包含 id 字段的对象
    const validBaseGroups = baseGroups.filter(bg => bg.id && bg.id > 0).map(bg => ({ id: bg.id }));

    const entity = {
      ...questionSectionGroupEntity,
      ...values,
      grade: studentGrades.find(it => it.id.toString() === values.grade?.toString()),
      baseGroupIds: JSON.stringify(validBaseGroups), // 保存只包含 id 的对象数组
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const handleViewBaseGroup = (baseGroupId: string) => {
    setSelectedBaseGroupId(baseGroupId);
    setShowError(false);
    setModalOpen(false);
    dispatch(getQuestionBaseGroupEntity(baseGroupId));
  };

  const toggleModal = () => {
    setModalOpen(!modalOpen);
    setSelectedBaseGroupId(null);
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...questionSectionGroupEntity,
          grade: questionSectionGroupEntity?.grade?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.questionSectionGroup.home.createOrEditLabel" data-cy="QuestionSectionGroupCreateUpdateHeading">
            管理题目章节
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
                <ValidatedField name="id" required readOnly id="question-section-group-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Title" id="question-section-group-title" name="title" data-cy="title" type="text" />
              <h5>基础分组管理</h5>
              {showError && (
                <Alert color="danger" style={{ marginBottom: '15px' }}>
                  {errorMessage}
                </Alert>
              )}
              <div>
                {baseGroups.map((baseGroup, index) => (
                  <Row
                    key={baseGroup.id || index}
                    className="align-items-end"
                    style={{ marginBottom: '10px', padding: '10px', border: '1px solid #eee', borderRadius: '5px' }}
                  >
                    <Col md="8">
                      <TextField
                        fullWidth
                        label={`基础分组ID #${index + 1}`}
                        value={baseGroup.id || ''}
                        onChange={e => {
                          const newBaseGroups = [...baseGroups];
                          newBaseGroups[index] = { ...newBaseGroups[index], id: Number(e.target.value) || 0 };
                          setBaseGroups(newBaseGroups);
                        }}
                        placeholder="请输入基础分组ID..."
                        type="number"
                      />
                    </Col>
                    <Col md="2">
                      {baseGroup.id && baseGroup.id > 0 && (
                        <Button color="info" onClick={() => handleViewBaseGroup(String(baseGroup.id))} disabled={!baseGroup.id}>
                          查看详情
                        </Button>
                      )}
                    </Col>
                    <Col md="2">
                      <Button
                        color="danger"
                        onClick={() => {
                          const newBaseGroups = baseGroups.filter((_, i) => i !== index);
                          setBaseGroups(newBaseGroups.length > 0 ? newBaseGroups : []);
                        }}
                      >
                        删除
                      </Button>
                    </Col>
                  </Row>
                ))}
                <Button
                  color="secondary"
                  onClick={() =>
                    setBaseGroups([
                      ...baseGroups,
                      {
                        id: 0,
                        title: `基础分组 ${baseGroups.length + 1}`,
                        description: null,
                        order: baseGroups.length + 1,
                        isActive: true,
                        createdAt: new Date().toISOString(),
                        updatedAt: new Date().toISOString(),
                        metadata: null,
                      },
                    ])
                  }
                  style={{ marginTop: '10px' }}
                >
                  增加基础分组
                </Button>
              </div>
              <ValidatedField id="question-section-group-grade" name="grade" data-cy="grade" label="Grade" type="select">
                <option value="" key="0" />
                {studentGrades
                  ? studentGrades.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question-section-group" replace color="info">
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

      {/* Base Group Detail Modal */}
      <Modal isOpen={modalOpen} toggle={toggleModal} size="lg">
        <ModalHeader toggle={toggleModal}>基础分组详情 - ID: {selectedBaseGroupId}</ModalHeader>
        <ModalBody>
          {questionBaseGroupLoading ? (
            <div className="text-center">
              <p>正在加载基础分组详情...</p>
            </div>
          ) : questionBaseGroupEntity ? (
            <div>
              <Row>
                <Col md="12">
                  <strong>标题:</strong> {questionBaseGroupEntity.title || 'N/A'}
                </Col>
              </Row>
              <hr />
              <Row>
                <Col md="12">
                  <strong>题目ID列表:</strong>
                  <div
                    style={{
                      border: '1px solid #ddd',
                      padding: '15px',
                      borderRadius: '5px',
                      backgroundColor: '#f9f9f9',
                      minHeight: '50px',
                      marginTop: '10px',
                      fontFamily: 'monospace',
                      whiteSpace: 'pre-wrap',
                    }}
                  >
                    {questionBaseGroupEntity.questionIds || 'N/A'}
                  </div>
                </Col>
              </Row>
            </div>
          ) : (
            <div className="text-center">
              <p>无法加载基础分组详情</p>
            </div>
          )}
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleModal}>
            关闭
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default QuestionSectionGroupUpdate;
