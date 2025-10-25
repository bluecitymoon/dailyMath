import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Row, Col, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { JhiItemCount, JhiPagination, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp, faGripVertical } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { DragDropContext, Droppable, Draggable, DropResult } from 'react-beautiful-dnd';

import { getEntities, updateDisplayOrder } from './question-section-group.reducer';
import { getEntities as getStudentGrades } from 'app/entities/student-grade/student-grade.reducer';
import { getEntities as getQuestionBaseGroups } from 'app/entities/question-base-group/question-base-group.reducer';
import { IStudentGrade } from 'app/shared/model/student-grade.model';
import { IQuestionBaseGroup } from 'app/shared/model/question-base-group.model';
import EmptyStateMessage from 'app/shared/components/empty-state-message';

export const QuestionSectionGroup = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const questionSectionGroupList = useAppSelector(state => state.questionSectionGroup.entities);
  const loading = useAppSelector(state => state.questionSectionGroup.loading);
  const totalItems = useAppSelector(state => state.questionSectionGroup.totalItems);

  // 获取年级列表
  const studentGradeList = useAppSelector(state => state.studentGrade.entities);
  const studentGradeLoading = useAppSelector(state => state.studentGrade.loading);

  // 获取题目基础分组列表
  const questionBaseGroupList = useAppSelector(state => state.questionBaseGroup.entities);
  const questionBaseGroupLoading = useAppSelector(state => state.questionBaseGroup.loading);

  // 过滤状态
  const [selectedGradeId, setSelectedGradeId] = useState<number | null>(null);
  const [filteredGroups, setFilteredGroups] = useState(questionSectionGroupList);

  // 模态框状态
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedBaseGroupIds, setSelectedBaseGroupIds] = useState<string>('');
  const [displayedBaseGroups, setDisplayedBaseGroups] = useState<IQuestionBaseGroup[]>([]);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  // 加载年级数据
  useEffect(() => {
    if (studentGradeList.length === 0 && !studentGradeLoading) {
      dispatch(getStudentGrades({ page: 0, size: 1000, sort: 'id,asc' }));
    }
  }, [dispatch, studentGradeList.length, studentGradeLoading]);

  // 加载题目基础分组数据
  useEffect(() => {
    if (questionBaseGroupList.length === 0 && !questionBaseGroupLoading) {
      dispatch(getQuestionBaseGroups({ page: 0, size: 10000, sort: 'id,asc' }));
    }
  }, [dispatch, questionBaseGroupList.length, questionBaseGroupLoading]);

  // 根据年级过滤题目章节分组
  useEffect(() => {
    if (!selectedGradeId) {
      setFilteredGroups(questionSectionGroupList);
      return;
    }

    const filtered = questionSectionGroupList.filter(group => {
      return group.grade && group.grade.id === selectedGradeId;
    });

    setFilteredGroups(filtered);
  }, [selectedGradeId, questionSectionGroupList]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const handleGradeChange = (gradeId: number | null) => {
    setSelectedGradeId(gradeId);
  };

  // 处理查看分组按钮点击
  const handleViewGroups = (baseGroupIds: string) => {
    setSelectedBaseGroupIds(baseGroupIds);
    setIsModalOpen(true);

    // 解析baseGroupIds并找到对应的基础分组
    try {
      let groupIds: number[] = [];

      // 首先尝试解析 JSON 格式
      try {
        const parsedGroups = JSON.parse(baseGroupIds);
        if (Array.isArray(parsedGroups)) {
          groupIds = parsedGroups.map(item => (typeof item === 'object' ? item.id : Number(item)));
        }
      } catch (jsonError) {
        // 如果 JSON 解析失败，尝试按逗号分隔的字符串格式解析
        groupIds = baseGroupIds ? baseGroupIds.split(',').map(id => parseInt(id.trim(), 10)) : [];
      }

      const groups = questionBaseGroupList.filter(group => group.id && groupIds.includes(group.id));
      setDisplayedBaseGroups(groups);
    } catch (error) {
      console.error('Error parsing baseGroupIds:', error);
      setDisplayedBaseGroups([]);
    }
  };

  // 关闭模态框
  const toggleModal = () => {
    setIsModalOpen(!isModalOpen);
  };

  // 打开基础分组详情页面
  const handleViewBaseGroupDetail = (baseGroupId: number) => {
    const url = `/question-base-group/${baseGroupId}/edit?page=1&sort=id,asc`;
    window.open(url, '_blank');
  };

  const handleDragEnd = (result: DropResult) => {
    const { destination, source } = result;

    // 如果没有目标位置，不做任何操作
    if (!destination) {
      return;
    }

    // 如果拖拽到相同位置，不做任何操作
    if (destination.index === source.index) {
      return;
    }

    // 检查是否选择了年级过滤
    if (!selectedGradeId) {
      alert('请先选择年级过滤，然后才能进行拖拽排序！');
      return;
    }

    // 创建新的数组并重新排序
    const newFilteredGroups = Array.from(filteredGroups);
    const [reorderedItem] = newFilteredGroups.splice(source.index, 1);
    newFilteredGroups.splice(destination.index, 0, reorderedItem);

    // 更新过滤后的分组列表
    setFilteredGroups(newFilteredGroups);

    // 在年级过滤模式下，拖拽排序只影响本地显示，不保存到后端
    // 因为过滤后的数据可能不是完整的数据集
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="question-section-group-heading" data-cy="QuestionSectionGroupHeading">
        题目章节分组
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> 刷新列表
          </Button>
          <Link
            to="/question-section-group/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; 管理章节
          </Link>
        </div>
      </h2>

      {/* 年级过滤区域 */}
      <Row className="mb-3">
        <Col md="4">
          <label htmlFor="grade-filter" className="form-label">
            <FontAwesomeIcon icon="filter" /> 按年级过滤
          </label>
          <select
            id="grade-filter"
            className="form-select"
            value={selectedGradeId || ''}
            onChange={e => handleGradeChange(e.target.value ? Number(e.target.value) : null)}
            disabled={studentGradeLoading}
          >
            <option value="">所有年级</option>
            {studentGradeList.map((grade: IStudentGrade) => (
              <option key={grade.id} value={grade.id}>
                {grade.name}
              </option>
            ))}
          </select>
        </Col>
        <Col md="8" className="d-flex align-items-end">
          {selectedGradeId && (
            <div className="text-muted">
              已选择年级: {studentGradeList.find((g: IStudentGrade) => g.id === selectedGradeId)?.name}
              <Button color="link" size="sm" onClick={() => handleGradeChange(null)} className="ms-2">
                清除过滤
              </Button>
            </div>
          )}
        </Col>
      </Row>

      {/* 拖拽提示信息 */}
      {filteredGroups && filteredGroups.length > 0 && (
        <div className="alert alert-info mb-3" role="alert">
          <FontAwesomeIcon icon="info-circle" className="me-2" />
          {selectedGradeId ? (
            <span>已选择年级过滤，可以拖拽左侧图标进行排序（仅影响当前显示）</span>
          ) : (
            <span>请先选择年级过滤，然后才能进行拖拽排序</span>
          )}
        </div>
      )}

      <div className="table-responsive">
        {filteredGroups && filteredGroups.length > 0 ? (
          <DragDropContext onDragEnd={handleDragEnd}>
            <Table responsive>
              <thead>
                <tr>
                  <th style={{ width: '40px' }}>拖拽排序</th>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('title')}>
                    章节 <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                  </th>
                  <th className="hand" onClick={sort('baseGroupIds')}>
                    分组列表 <FontAwesomeIcon icon={getSortIconByFieldName('baseGroupIds')} />
                  </th>
                  <th>
                    年级 <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <Droppable droppableId="question-section-groups">
                {(provided, snapshot) => (
                  <tbody
                    ref={provided.innerRef}
                    {...provided.droppableProps}
                    style={{
                      backgroundColor: snapshot.isDraggingOver ? '#f8f9fa' : 'transparent',
                    }}
                  >
                    {filteredGroups.map((questionSectionGroup, i) => (
                      <Draggable
                        key={questionSectionGroup.id}
                        draggableId={questionSectionGroup.id.toString()}
                        index={i}
                        isDragDisabled={!selectedGradeId}
                      >
                        {(draggableProvided, draggableSnapshot) => (
                          <tr
                            ref={draggableProvided.innerRef}
                            {...draggableProvided.draggableProps}
                            data-cy="entityTable"
                            style={{
                              backgroundColor: draggableSnapshot.isDragging ? '#e3f2fd' : 'transparent',
                              ...draggableProvided.draggableProps.style,
                            }}
                          >
                            <td
                              {...(selectedGradeId ? draggableProvided.dragHandleProps : {})}
                              style={{ cursor: selectedGradeId ? 'grab' : 'default' }}
                            >
                              <FontAwesomeIcon
                                icon={faGripVertical}
                                className={selectedGradeId ? 'text-muted' : 'text-muted opacity-50'}
                                title={selectedGradeId ? '拖拽排序' : '请先选择年级过滤'}
                              />
                            </td>
                            <td>
                              <Button tag={Link} to={`/question-section-group/${questionSectionGroup.id}`} color="link" size="sm">
                                {questionSectionGroup.id}
                              </Button>
                            </td>
                            <td>{questionSectionGroup.title}</td>
                            <td>{questionSectionGroup.baseGroupIds}</td>
                            <td>
                              {questionSectionGroup.grade ? (
                                <Link to={`/student-grade/${questionSectionGroup.grade.id}`}>{questionSectionGroup.grade.name}</Link>
                              ) : (
                                ''
                              )}
                            </td>
                            <td className="text-end">
                              <div className="btn-group flex-btn-group-container">
                                <Button
                                  tag={Link}
                                  to={`/question-section-group/${questionSectionGroup.id}`}
                                  color="info"
                                  size="sm"
                                  data-cy="entityDetailsButton"
                                >
                                  <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">查看</span>
                                </Button>
                                <Button
                                  onClick={() => handleViewGroups(questionSectionGroup.baseGroupIds || '')}
                                  color="secondary"
                                  size="sm"
                                  data-cy="entityViewGroupsButton"
                                  disabled={!questionSectionGroup.baseGroupIds}
                                >
                                  <FontAwesomeIcon icon="list" /> <span className="d-none d-md-inline">查看分组</span>
                                </Button>
                                <Button
                                  tag={Link}
                                  to={`/question-section-group/${questionSectionGroup.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                                  color="primary"
                                  size="sm"
                                  data-cy="entityEditButton"
                                >
                                  <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">编辑</span>
                                </Button>
                                <Button
                                  onClick={() =>
                                    (window.location.href = `/question-section-group/${questionSectionGroup.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                                  }
                                  color="danger"
                                  size="sm"
                                  data-cy="entityDeleteButton"
                                >
                                  <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">删除</span>
                                </Button>
                              </div>
                            </td>
                          </tr>
                        )}
                      </Draggable>
                    ))}
                    {provided.placeholder}
                  </tbody>
                )}
              </Droppable>
            </Table>
          </DragDropContext>
        ) : (
          <EmptyStateMessage message={selectedGradeId ? '未找到符合条件的题目章节分组' : '未找到题目章节分组'} loading={loading} />
        )}
      </div>
      {selectedGradeId ? (
        // 过滤模式下不显示分页，显示过滤结果统计
        <div className="text-center text-muted mt-3">显示 {filteredGroups.length} 个符合条件的题目章节分组</div>
      ) : totalItems ? (
        // 正常模式下显示分页
        <div className={questionSectionGroupList && questionSectionGroupList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}

      {/* 查看分组模态框 */}
      <Modal isOpen={isModalOpen} toggle={toggleModal} size="lg">
        <ModalHeader toggle={toggleModal}>
          <FontAwesomeIcon icon="list" className="me-2" />
          查看分组列表
        </ModalHeader>
        <ModalBody>
          {displayedBaseGroups.length > 0 ? (
            <div className="table-responsive">
              <Table striped>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>分组名称</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  {displayedBaseGroups.map(baseGroup => (
                    <tr key={baseGroup.id}>
                      <td>{baseGroup.id}</td>
                      <td>{baseGroup.title}</td>
                      <td>
                        <Button color="primary" size="sm" onClick={() => handleViewBaseGroupDetail(baseGroup.id)}>
                          <FontAwesomeIcon icon="eye" /> 查看详情
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          ) : (
            <div className="text-center text-muted py-4">
              <FontAwesomeIcon icon="exclamation-circle" className="me-2" />
              没有找到相关的基础分组数据
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

export default QuestionSectionGroup;
