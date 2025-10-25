import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, Modal, ModalHeader, ModalBody, ModalFooter, Alert } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import TextField from '@mui/material/TextField';
import { IQuestion } from 'app/shared/model/question.model';
import { IQuestionGroupItem } from 'app/shared/model/question-group-item.model';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './question-base-group.reducer';
import { getEntity as getQuestionEntity } from 'app/entities/question/question.reducer';

export const QuestionBaseGroupUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const questionBaseGroupEntity = useAppSelector(state => state.questionBaseGroup.entity);
  const loading = useAppSelector(state => state.questionBaseGroup.loading);
  const updating = useAppSelector(state => state.questionBaseGroup.updating);
  const updateSuccess = useAppSelector(state => state.questionBaseGroup.updateSuccess);
  const questionEntity = useAppSelector(state => state.question.entity);
  const questionLoading = useAppSelector(state => state.question.loading);
  const questionError = useAppSelector(state => state.question.errorMessage);

  const [questions, setQuestions] = useState<IQuestionGroupItem[]>([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedQuestionId, setSelectedQuestionId] = useState<string | null>(null);
  const [showError, setShowError] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [formValues, setFormValues] = useState<{ title?: string; [key: string]: any }>({});

  const handleClose = () => {
    navigate(`/question-base-group${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  // 初始化表单值
  useEffect(() => {
    if (!isNew && questionBaseGroupEntity) {
      setFormValues({
        ...questionBaseGroupEntity,
      });
    } else if (isNew) {
      setFormValues({});
    }
  }, [questionBaseGroupEntity, isNew]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew && questionBaseGroupEntity?.questionIds) {
      try {
        // 解析 questionIds 中的对象数组
        const parsedQuestions = JSON.parse(questionBaseGroupEntity.questionIds);
        if (Array.isArray(parsedQuestions)) {
          // 将对象数组转换为完整的 question 对象用于界面显示
          setQuestions(
            parsedQuestions.map((item, index) => ({
              id: item.id || Number(item),
              title: `题目 ${index + 1}`,
              description: null,
              order: index + 1,
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
            })),
          );
        } else {
          setQuestions([]);
        }
      } catch (error) {
        setQuestions([]);
      }
    } else if (isNew) {
      setQuestions([]);
    }
  }, [questionBaseGroupEntity, isNew]);

  // 处理 question 加载成功
  useEffect(() => {
    if (questionEntity && selectedQuestionId && !questionLoading) {
      setModalOpen(true);
      setShowError(false);
    }
  }, [questionEntity, selectedQuestionId, questionLoading]);

  // 处理 question 加载失败
  useEffect(() => {
    if (questionError && selectedQuestionId && !questionLoading) {
      setShowError(true);
      setErrorMessage(`题目 ID ${selectedQuestionId} 不存在或查询失败`);
      setModalOpen(false);
    }
  }, [questionError, selectedQuestionId, questionLoading]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    // 过滤掉无效的题目，只保留包含 id 字段的对象
    const validQuestions = questions.filter(q => q.id && q.id > 0).map(q => ({ id: q.id }));

    const entity = {
      ...questionBaseGroupEntity,
      ...values,
      questionIds: JSON.stringify(validQuestions), // 保存只包含 id 的对象数组
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const handleViewQuestion = (questionId: string) => {
    setSelectedQuestionId(questionId);
    setShowError(false);
    setModalOpen(false);
    dispatch(getQuestionEntity(questionId));
  };

  const toggleModal = () => {
    setModalOpen(!modalOpen);
    setSelectedQuestionId(null);
  };

  const defaultValues = () => {
    return formValues;
  };

  const handleFormChange = (fieldName: string, value: any) => {
    setFormValues(prev => ({
      ...prev,
      [fieldName]: value,
    }));
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.questionBaseGroup.home.createOrEditLabel" data-cy="QuestionBaseGroupCreateUpdateHeading">
            创建或编辑 Question Base Group
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
                <ValidatedField name="id" required readOnly id="question-base-group-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Title"
                id="question-base-group-title"
                name="title"
                data-cy="title"
                type="text"
                value={formValues.title || ''}
                onChange={e => handleFormChange('title', e.target.value)}
              />
              <h5>题目管理</h5>
              {showError && (
                <Alert color="danger" style={{ marginBottom: '15px' }}>
                  {errorMessage}
                </Alert>
              )}
              <div>
                {questions.map((question, index) => (
                  <Row
                    key={question.id || index}
                    className="align-items-end"
                    style={{ marginBottom: '10px', padding: '10px', border: '1px solid #eee', borderRadius: '5px' }}
                  >
                    <Col md="8">
                      <TextField
                        fullWidth
                        label={`题目ID #${index + 1}`}
                        value={question.id || ''}
                        onChange={e => {
                          const newQuestions = [...questions];
                          newQuestions[index] = { ...newQuestions[index], id: Number(e.target.value) || 0 };
                          setQuestions(newQuestions);
                        }}
                        placeholder="请输入题目ID..."
                        type="number"
                      />
                    </Col>
                    <Col md="2">
                      {question.id && question.id > 0 && (
                        <Button color="info" onClick={() => handleViewQuestion(String(question.id))} disabled={!question.id}>
                          查看详情
                        </Button>
                      )}
                    </Col>
                    <Col md="2">
                      <Button
                        color="danger"
                        onClick={() => {
                          const newQuestions = questions.filter((_, i) => i !== index);
                          setQuestions(newQuestions.length > 0 ? newQuestions : []);
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
                    setQuestions([
                      ...questions,
                      {
                        id: 0,
                        title: `题目 ${questions.length + 1}`,
                        description: null,
                        order: questions.length + 1,
                        points: null,
                        difficulty: null,
                        category: null,
                        type: null,
                        grade: null,
                        tags: null,
                        isActive: true,
                        createdAt: new Date().toISOString(),
                        updatedAt: new Date().toISOString(),
                        metadata: null,
                      },
                    ])
                  }
                  style={{ marginTop: '10px' }}
                >
                  增加题目
                </Button>
              </div>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question-base-group" replace color="info">
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

      {/* Question Detail Modal */}
      <Modal isOpen={modalOpen} toggle={toggleModal} size="lg">
        <ModalHeader toggle={toggleModal}>题目详情 - ID: {selectedQuestionId}</ModalHeader>
        <ModalBody>
          {questionLoading ? (
            <div className="text-center">
              <p>正在加载题目详情...</p>
            </div>
          ) : questionEntity ? (
            <div>
              <Row>
                <Col md="6">
                  <strong>积分:</strong> {questionEntity.points || 'N/A'}
                </Col>
                <Col md="6">
                  <strong>难度级别:</strong> {questionEntity.level || 'N/A'}
                </Col>
              </Row>
              <hr />
              <Row>
                <Col md="6">
                  <strong>分类:</strong> {questionEntity.questionCategory?.name || 'N/A'}
                </Col>
                <Col md="6">
                  <strong>类型:</strong> {questionEntity.type?.name || 'N/A'}
                </Col>
              </Row>
              <Row>
                <Col md="12">
                  <strong>年级:</strong> {questionEntity.grade?.name || 'N/A'}
                </Col>
              </Row>
              <hr />
              <div>
                <strong>题目描述:</strong>
                <div
                  dangerouslySetInnerHTML={{ __html: questionEntity.description || '' }}
                  style={{
                    border: '1px solid #ddd',
                    padding: '15px',
                    borderRadius: '5px',
                    backgroundColor: '#f9f9f9',
                    minHeight: '100px',
                    marginTop: '10px',
                  }}
                />
              </div>
              <div style={{ marginTop: '20px' }}>
                <strong>解题思路:</strong>
                <div
                  dangerouslySetInnerHTML={{ __html: questionEntity.solution || '' }}
                  style={{
                    border: '1px solid #ddd',
                    padding: '15px',
                    borderRadius: '5px',
                    backgroundColor: '#f9f9f9',
                    minHeight: '100px',
                    marginTop: '10px',
                  }}
                />
              </div>
              {questionEntity.solutionExternalLink && (
                <div style={{ marginTop: '20px' }}>
                  <strong>解题思路链接:</strong>
                  <a href={questionEntity.solutionExternalLink} target="_blank" rel="noopener noreferrer">
                    {questionEntity.solutionExternalLink}
                  </a>
                </div>
              )}
              {questionEntity.options && questionEntity.options.length > 0 && (
                <div style={{ marginTop: '20px' }}>
                  <strong>选项:</strong>
                  <div style={{ marginTop: '10px' }}>
                    {questionEntity.options.map((option, index) => (
                      <div
                        key={index}
                        style={{
                          border: '1px solid #eee',
                          padding: '10px',
                          marginBottom: '5px',
                          borderRadius: '3px',
                          backgroundColor: option.isAnswer ? '#e8f5e8' : '#f9f9f9',
                        }}
                      >
                        <Row>
                          <Col md="8">
                            <strong>选项 {index + 1}:</strong> {option.name || 'N/A'}
                            {option.type === 2 && option.imageUrl && (
                              <div style={{ marginTop: '5px' }}>
                                <img
                                  src={option.imageUrl}
                                  alt={`选项 ${index + 1}`}
                                  style={{ maxWidth: '200px', maxHeight: '100px', border: '1px solid #ddd' }}
                                />
                              </div>
                            )}
                          </Col>
                          <Col md="4" className="text-right">
                            {option.isAnswer && <span style={{ color: 'green', fontWeight: 'bold' }}>✓ 正确答案</span>}
                          </Col>
                        </Row>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="text-center">
              <p>无法加载题目详情</p>
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

export default QuestionBaseGroupUpdate;
