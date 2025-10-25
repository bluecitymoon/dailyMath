import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, FormGroup, Label, Input } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import Lightbox from 'react-image-lightbox';
import 'react-image-lightbox/style.css';
import { IQuestionCategory } from 'app/shared/model/question-category.model';
import { IQuestionType } from 'app/shared/model/question-type.model';
import { IStudentGrade } from 'app/shared/model/student-grade.model';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getQuestionCategories } from 'app/entities/question-category/question-category.reducer';
import { getEntities as getQuestionTypes } from 'app/entities/question-type/question-type.reducer';
import { getEntities as getStudentGrades } from 'app/entities/student-grade/student-grade.reducer';
import { createEntity, getEntity, reset, updateEntity } from './question.reducer';
import { IQuestionOption } from 'app/shared/model/question-option.model';
import ImageUpload from 'app/shared/components/image-upload';
import QuestionDescImageUpload from 'app/shared/components/question-desc-image-upload';

// 注册自定义字间距属性
const Parchment = Quill.import('parchment') as any;
const LetterSpacingStyle = new Parchment.Attributor.Style('letterSpacing', 'letter-spacing', {
  scope: Parchment.Scope.INLINE,
  whitelist: ['normal', '0.05em', '0.1em', '0.15em', '0.2em', '0.3em', '0.5em'],
});
Quill.register(LetterSpacingStyle, true);

export const QuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const questionCategories = useAppSelector(state => state.questionCategory.entities);
  const questionTypes = useAppSelector(state => state.questionType.entities);
  const studentGrades = useAppSelector(state => state.studentGrade.entities);
  const questionEntity = useAppSelector(state => state.question.entity);
  const loading = useAppSelector(state => state.question.loading);
  const updating = useAppSelector(state => state.question.updating);
  const updateSuccess = useAppSelector(state => state.question.updateSuccess);
  const [selectedCategory, setSelectedCategory] = useState<IQuestionCategory | null>(null);
  const [selectedType, setSelectedType] = useState<IQuestionType | null>(null);
  const [selectedGrade, setSelectedGrade] = useState<IStudentGrade | null>(null);
  const [description, setDescription] = useState('');
  const [solution, setSolution] = useState('');
  const [options, setOptions] = useState<IQuestionOption[]>([]);
  const [answers, setAnswers] = useState<string[]>(['']);
  const [lightboxOpenIndex, setLightboxOpenIndex] = useState<number | null>(null);
  const [quillEditorRef, setQuillEditorRef] = useState<any>(null);
  const [shouldNavigate, setShouldNavigate] = useState(true);

  // Quill editor modules and formats
  const quillModules = {
    toolbar: [
      [{ header: [1, 2, 3, false] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ color: [] }, { background: [] }],
      [{ align: [] }],
      [{ letterSpacing: ['normal', '0.05em', '0.1em', '0.15em', '0.2em', '0.3em', '0.5em'] }],
      ['link', 'image'],
      ['clean'],
    ],
  };
  const quillFormats = [
    'header',
    'bold',
    'italic',
    'underline',
    'strike',
    'list',
    'bullet',
    'color',
    'background',
    'align',
    'letterSpacing',
    'link',
    'image',
  ];

  const handleClose = () => {
    navigate(`/question${location.search}`);
  };

  const handleImageUpload = (imageUrl: string) => {
    try {
      if (quillEditorRef && quillEditorRef.getEditor) {
        const editor = quillEditorRef.getEditor();
        if (editor) {
          const range = editor.getSelection();
          if (range) {
            editor.insertEmbed(range.index, 'image', imageUrl);
            editor.setSelection(range.index + 1);
          } else {
            // 如果没有选中范围，插入到末尾
            editor.insertEmbed(editor.getLength(), 'image', imageUrl);
          }
        }
      } else {
        console.warn('Quill editor not ready yet');
        // 如果编辑器还没准备好，延迟重试
        setTimeout(() => handleImageUpload(imageUrl), 100);
      }
    } catch (error) {
      console.error('Error inserting image:', error);
      alert('插入图片失败，请重试');
    }
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuestionCategories({}));
    dispatch(getQuestionTypes({}));
    dispatch(getStudentGrades({}));
  }, []);

  useEffect(() => {
    if (updateSuccess && shouldNavigate) {
      handleClose();
    }
  }, [updateSuccess, shouldNavigate]);

  useEffect(() => {
    if (!isNew && questionEntity?.questionCategory?.id && questionCategories.length > 0) {
      const found = questionCategories.find(it => it.id === questionEntity.questionCategory.id) || null;
      setSelectedCategory(found);
    }
    if (!isNew && questionEntity?.type?.id && questionTypes.length > 0) {
      const found = questionTypes.find(it => it.id === questionEntity.type.id) || null;
      setSelectedType(found);
    }
    if (!isNew && questionEntity?.grade?.id && studentGrades.length > 0) {
      const found = studentGrades.find(it => it.id === questionEntity.grade.id) || null;
      setSelectedGrade(found);
    }
  }, [questionEntity, questionCategories, questionTypes, studentGrades, isNew]);

  useEffect(() => {
    if (!isNew && questionEntity?.description) {
      setDescription(questionEntity.description);
    }
    if (!isNew && questionEntity?.solution) {
      setSolution(questionEntity.solution);
    }
    if (!isNew && Array.isArray(questionEntity?.options)) {
      setOptions(questionEntity.options as IQuestionOption[]);
    }
    if (!isNew && questionEntity?.answer) {
      try {
        // 尝试解析JSON字符串为数组
        const parsedAnswers = JSON.parse(questionEntity.answer);
        if (Array.isArray(parsedAnswers)) {
          setAnswers(parsedAnswers.length > 0 ? parsedAnswers : ['']);
        } else {
          // 如果不是数组，将单个答案放入数组
          setAnswers([questionEntity.answer]);
        }
      } catch (error) {
        // 如果解析失败，将原始答案作为单个元素
        setAnswers([questionEntity.answer]);
      }
    } else if (isNew) {
      setAnswers(['']);
    }
  }, [questionEntity, isNew]);

  const saveEntity = values => {
    setShouldNavigate(true); // 正常保存后跳转页面
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.points === '') {
      values.points = null;
    } else if (values.points !== undefined && typeof values.points !== 'number') {
      values.points = Number(values.points);
    }
    if (values.level === '') {
      values.level = null;
    } else if (values.level !== undefined && typeof values.level !== 'number') {
      values.level = Number(values.level);
    }
    values.solutionExternalLink = values.solutionExternalLink?.trim() || null;
    values.createDate = convertDateTimeToServer(values.createDate);
    values.updateDate = convertDateTimeToServer(values.updateDate);
    if (values.createByUserId !== undefined && typeof values.createByUserId !== 'number') {
      values.createByUserId = Number(values.createByUserId);
    }

    // 将多个答案转换为JSON字符串并转义
    const answersJson = JSON.stringify(answers.filter(answer => answer.trim() !== ''));

    const entity = {
      ...questionEntity,
      ...values,
      description, // Use the rich text content
      solution, // Use the rich text content for solution
      answer: answersJson, // 使用转换后的JSON字符串
      questionCategory: selectedCategory,
      type: selectedType,
      grade: selectedGrade,
      options:
        options?.map(o => ({
          id: o.id,
          name: (o.name ?? '').trim() || null,
          type: o.type ?? null,
          imageUrl: o.imageUrl ?? null,
          isAnswer: o.isAnswer ?? false,
        })) || [],
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const saveEntityOnly = values => {
    setShouldNavigate(false); // 仅保存，不跳转页面
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.points === '') {
      values.points = null;
    } else if (values.points !== undefined && typeof values.points !== 'number') {
      values.points = Number(values.points);
    }
    if (values.level === '') {
      values.level = null;
    } else if (values.level !== undefined && typeof values.level !== 'number') {
      values.level = Number(values.level);
    }
    values.solutionExternalLink = values.solutionExternalLink?.trim() || null;
    values.createDate = convertDateTimeToServer(values.createDate);
    values.updateDate = convertDateTimeToServer(values.updateDate);
    if (values.createByUserId !== undefined && typeof values.createByUserId !== 'number') {
      values.createByUserId = Number(values.createByUserId);
    }

    // 将多个答案转换为JSON字符串并转义
    const answersJson = JSON.stringify(answers.filter(answer => answer.trim() !== ''));

    const entity = {
      ...questionEntity,
      ...values,
      description, // Use the rich text content
      solution, // Use the rich text content for solution
      answer: answersJson, // 使用转换后的JSON字符串
      questionCategory: selectedCategory,
      type: selectedType,
      grade: selectedGrade,
      options:
        options?.map(o => ({
          id: o.id,
          name: (o.name ?? '').trim() || null,
          type: o.type ?? null,
          imageUrl: o.imageUrl ?? null,
          isAnswer: o.isAnswer ?? false,
        })) || [],
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
          ...questionEntity,
          // Ensure fields render correctly in inputs
          points: questionEntity.points ?? '',
          level: questionEntity.level ?? '',
          solutionExternalLink: questionEntity.solutionExternalLink ?? '',
          createDate: convertDateTimeFromServer(questionEntity.createDate),
          updateDate: convertDateTimeFromServer(questionEntity.updateDate),
        };

  return (
    <div>
      <style>
        {`
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item::before {
            content: '字间距';
          }
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label[data-value="normal"]::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item[data-value="normal"]::before {
            content: '正常';
          }
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label[data-value="0.05em"]::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item[data-value="0.05em"]::before {
            content: '最小';
            letter-spacing: 0.05em;
          }
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label[data-value="0.1em"]::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item[data-value="0.1em"]::before {
            content: '较小';
            letter-spacing: 0.1em;
          }
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label[data-value="0.15em"]::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item[data-value="0.15em"]::before {
            content: '中等';
            letter-spacing: 0.15em;
          }
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label[data-value="0.2em"]::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item[data-value="0.2em"]::before {
            content: '较大';
            letter-spacing: 0.2em;
          }
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label[data-value="0.3em"]::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item[data-value="0.3em"]::before {
            content: '大';
            letter-spacing: 0.3em;
          }
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-label[data-value="0.5em"]::before,
          .ql-snow .ql-picker.ql-letterSpacing .ql-picker-item[data-value="0.5em"]::before {
            content: '最大';
            letter-spacing: 0.5em;
          }
        `}
      </style>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailyMathApp.question.home.createOrEditLabel" data-cy="QuestionCreateUpdateHeading">
            管理题目
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>加载中...</p>
          ) : (
            <ValidatedForm key={isNew ? 'create' : questionEntity.id} defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="question-id" label="ID" validate={{ required: true }} /> : null}
              <Row>
                <Col md="4">
                  <Autocomplete
                    options={questionCategories || []}
                    getOptionLabel={option => option.name ?? ''}
                    isOptionEqualToValue={(option, value) => option.id === value.id}
                    value={selectedCategory}
                    onChange={(event, newValue) => setSelectedCategory(newValue)}
                    renderInput={params => <TextField {...params} label="问题分类" />}
                  />
                </Col>
                <Col md="4">
                  <Autocomplete
                    options={questionTypes || []}
                    getOptionLabel={option => option.name ?? ''}
                    isOptionEqualToValue={(option, value) => option.id === value.id}
                    value={selectedType}
                    onChange={(event, newValue) => setSelectedType(newValue)}
                    renderInput={params => <TextField {...params} label="题目类型" />}
                  />
                </Col>
                <Col md="4">
                  <Autocomplete
                    options={studentGrades || []}
                    getOptionLabel={option => option.name ?? ''}
                    isOptionEqualToValue={(option, value) => option.id === value.id}
                    value={selectedGrade}
                    onChange={(event, newValue) => setSelectedGrade(newValue)}
                    renderInput={params => <TextField {...params} label="年级" />}
                  />
                </Col>
              </Row>
              <br />
              <ValidatedField label="解题思路链接" id="question-solutionExternalLink" name="solutionExternalLink" type="text" />
              <ValidatedField label="积分" id="question-points" name="points" type="number" />
              <ValidatedField label="难度级别" id="question-level" name="level" type="number" step="0.1" min="0" />
              <FormGroup>
                <Label for="question-description">题目描述</Label>
                <div style={{ marginBottom: '10px' }}>
                  <QuestionDescImageUpload onUploadSuccess={handleImageUpload} />
                </div>
                <ReactQuill
                  id="question-description"
                  value={description}
                  onChange={setDescription}
                  modules={quillModules}
                  formats={quillFormats}
                  placeholder="请输入题目描述..."
                  style={{ height: '500px', marginBottom: '50px' }}
                  ref={setQuillEditorRef}
                />
              </FormGroup>
              <FormGroup>
                <Label for="question-solution">解题思路</Label>
                <ReactQuill
                  id="question-solution"
                  value={solution}
                  onChange={setSolution}
                  modules={quillModules}
                  formats={quillFormats}
                  placeholder="请输入题目解答..."
                  style={{ height: '500px', marginBottom: '50px' }}
                />
              </FormGroup>
              <h5>答案管理</h5>
              <div>
                {answers.map((answer, index) => (
                  <Row key={index} className="align-items-end" style={{ marginBottom: '10px' }}>
                    <Col md="10">
                      <TextField
                        fullWidth
                        label={`答案 #${index + 1}`}
                        value={answer}
                        onChange={e => {
                          const newAnswers = [...answers];
                          newAnswers[index] = e.target.value;
                          setAnswers(newAnswers);
                        }}
                        placeholder="请输入答案内容..."
                      />
                    </Col>
                    <Col md="2">
                      {answers.length > 1 && (
                        <Button
                          color="danger"
                          onClick={() => {
                            const newAnswers = answers.filter((_, i) => i !== index);
                            setAnswers(newAnswers.length > 0 ? newAnswers : ['']);
                          }}
                        >
                          删除
                        </Button>
                      )}
                    </Col>
                  </Row>
                ))}
                <Button color="secondary" onClick={() => setAnswers([...answers, ''])} style={{ marginTop: '10px' }}>
                  增加答案
                </Button>
              </div>
              <hr />
              <h5>选项管理</h5>
              <div>
                {options.map((opt, index) => (
                  <Row key={opt.id ?? index} className="align-items-end" style={{ marginBottom: '10px' }}>
                    <Col md="3">
                      <TextField
                        fullWidth
                        label={`选项名称 #${index + 1}`}
                        value={opt.name ?? ''}
                        onChange={e => {
                          const copy = [...options];
                          copy[index] = { ...copy[index], name: e.target.value };
                          setOptions(copy);
                        }}
                      />
                    </Col>
                    <Col md="2">
                      <TextField
                        select
                        fullWidth
                        label="类型"
                        value={opt.type ?? 1}
                        onChange={e => {
                          const val = e.target.value === '' ? null : Number(e.target.value);
                          const copy = [...options];
                          copy[index] = { ...copy[index], type: val };
                          setOptions(copy);
                        }}
                      >
                        <MenuItem value={1}>文字</MenuItem>
                        <MenuItem value={2}>图片</MenuItem>
                      </TextField>
                    </Col>
                    <Col md="1" className="d-flex flex-column align-items-center">
                      <Label for={`isAnswer-${index}`} style={{ fontSize: '0.875rem', marginBottom: '4px' }}>
                        答案
                      </Label>
                      <Input
                        type="checkbox"
                        id={`isAnswer-${index}`}
                        checked={opt.isAnswer ?? false}
                        onChange={e => {
                          const copy = [...options];
                          copy[index] = { ...copy[index], isAnswer: e.target.checked };
                          setOptions(copy);
                        }}
                        style={{ transform: 'scale(1.2)' }}
                      />
                    </Col>
                    <Col md="3">
                      <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <TextField
                          fullWidth
                          label="图片URL"
                          value={opt.imageUrl ?? ''}
                          onChange={e => {
                            const copy = [...options];
                            copy[index] = { ...copy[index], imageUrl: e.target.value };
                            setOptions(copy);
                          }}
                        />
                        <ImageUpload
                          onUploadSuccess={url => {
                            const copy = [...options];
                            copy[index] = { ...copy[index], imageUrl: url };
                            setOptions(copy);
                          }}
                        />
                      </div>
                    </Col>
                    <Col md="2">
                      {opt.imageUrl ? (
                        <img
                          src={opt.imageUrl}
                          alt={`预览 #${index + 1}`}
                          style={{ height: '64px', width: 'auto', borderRadius: '4px', border: '1px solid #e0e0e0', cursor: 'pointer' }}
                          onClick={() => setLightboxOpenIndex(index)}
                        />
                      ) : null}
                    </Col>
                    <Col md="1">
                      <Button color="danger" onClick={() => setOptions(options.filter((_, i) => i !== index))}>
                        删除
                      </Button>
                    </Col>
                  </Row>
                ))}
                <Button
                  color="secondary"
                  onClick={() => setOptions([...(options || []), { name: '', type: 1, imageUrl: null, isAnswer: false }])}
                  style={{ marginTop: '10px' }}
                >
                  添加选项
                </Button>
                {lightboxOpenIndex !== null && options[lightboxOpenIndex]?.imageUrl ? (
                  <Lightbox mainSrc={options[lightboxOpenIndex].imageUrl} onCloseRequest={() => setLightboxOpenIndex(null)} />
                ) : null}
              </div>
              <br />
              <div className="text-center" style={{ marginTop: '20px' }}>
                <Button
                  tag={Link}
                  id="cancel-save"
                  data-cy="entityCreateCancelButton"
                  to="/question"
                  replace
                  color="info"
                  size="lg"
                  style={{ marginRight: '15px' }}
                >
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">返回</span>
                </Button>
                <Button
                  color="success"
                  id="save-entity-only"
                  data-cy="entitySaveOnlyButton"
                  disabled={updating}
                  size="lg"
                  onClick={() => {
                    // 获取表单的默认值作为基础
                    const formValues = defaultValues();
                    saveEntityOnly(formValues);
                  }}
                  style={{ marginRight: '15px' }}
                >
                  <FontAwesomeIcon icon="save" />
                  &nbsp; 仅保存
                </Button>
                <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating} size="lg">
                  <FontAwesomeIcon icon="save" />
                  &nbsp; 保存
                </Button>
              </div>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default QuestionUpdate;
