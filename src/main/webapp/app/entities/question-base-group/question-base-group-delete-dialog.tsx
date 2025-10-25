import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntity } from './question-base-group.reducer';

export const QuestionBaseGroupDeleteDialog = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const questionBaseGroupEntity = useAppSelector(state => state.questionBaseGroup.entity);
  const updateSuccess = useAppSelector(state => state.questionBaseGroup.updateSuccess);

  const handleClose = () => {
    navigate(`/question-base-group${pageLocation.search}`);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(questionBaseGroupEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="questionBaseGroupDeleteDialogHeading">
        确认删除
      </ModalHeader>
      <ModalBody id="dailyMathApp.questionBaseGroup.delete.question">你确定要删除题目基础分组 {questionBaseGroupEntity.id} 吗？</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; 取消
        </Button>
        <Button id="jhi-confirm-delete-questionBaseGroup" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; 删除
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default QuestionBaseGroupDeleteDialog;
