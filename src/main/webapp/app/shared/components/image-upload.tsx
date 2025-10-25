import React, { useState, useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import Cropper from 'react-easy-crop';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUpload, faCrop, faCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';

interface ImageUploadProps {
  onUploadSuccess: (url: string) => void;
  disabled?: boolean;
}

interface CropArea {
  x: number;
  y: number;
  width: number;
  height: number;
}

const ImageUpload: React.FC<ImageUploadProps> = ({ onUploadSuccess, disabled = false }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [imageSrc, setImageSrc] = useState<string>('');
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [croppedAreaPixels, setCroppedAreaPixels] = useState<CropArea | null>(null);
  const [isUploading, setIsUploading] = useState(false);
  const [aspectRatio, setAspectRatio] = useState<number | undefined>(undefined);

  const onDrop = useCallback((acceptedFiles: File[]) => {
    const file = acceptedFiles[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        setImageSrc(reader.result as string);
        setIsModalOpen(true);
      };
      reader.readAsDataURL(file);
    }
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'image/*': ['.jpeg', '.jpg', '.png', '.gif', '.webp'],
    },
    multiple: false,
    disabled,
  });

  const onCropComplete = useCallback((croppedArea: any, croppedAreaPixelsParam: CropArea) => {
    setCroppedAreaPixels(croppedAreaPixelsParam);
  }, []);

  const handleCropAndUpload = () => {
    if (!imageSrc || !croppedAreaPixels) return;

    setIsUploading(true);
    try {
      // Create canvas to crop image
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d');
      const image = new Image();

      image.onload = () => {
        canvas.width = croppedAreaPixels.width;
        canvas.height = croppedAreaPixels.height;

        ctx?.drawImage(
          image,
          croppedAreaPixels.x,
          croppedAreaPixels.y,
          croppedAreaPixels.width,
          croppedAreaPixels.height,
          0,
          0,
          croppedAreaPixels.width,
          croppedAreaPixels.height,
        );

        // Convert canvas to blob
        canvas.toBlob(
          async blob => {
            if (!blob) return;

            // Create FormData for upload
            const formData = new FormData();
            formData.append('file', blob, 'cropped-image.png');
            formData.append('folder', 'options');
            formData.append('filename', uuidv4());
            formData.append('bizType', 'options');

            // Upload to server
            const response = await axios.post('/api/files/upload', formData, {
              headers: {
                'Content-Type': 'multipart/form-data',
              },
            });

            if (response.data && response.data.downloadUrl) {
              onUploadSuccess(response.data.downloadUrl);
              setIsModalOpen(false);
              setImageSrc('');
              setCrop({ x: 0, y: 0 });
              setZoom(1);
              setCroppedAreaPixels(null);
              setAspectRatio(undefined);
            }
          },
          'image/png',
          0.9,
        );
      };

      image.src = imageSrc;
    } catch (error) {
      console.error('Upload failed:', error);
      alert('上传失败，请重试');
    } finally {
      setIsUploading(false);
    }
  };

  const handleCancel = () => {
    setIsModalOpen(false);
    setImageSrc('');
    setCrop({ x: 0, y: 0 });
    setZoom(1);
    setCroppedAreaPixels(null);
    setAspectRatio(undefined);
  };

  return (
    <>
      <div
        {...getRootProps()}
        style={{
          border: '2px dashed #ccc',
          borderRadius: '4px',
          padding: '8px',
          textAlign: 'center',
          cursor: disabled ? 'not-allowed' : 'pointer',
          opacity: disabled ? 0.6 : 1,
          backgroundColor: isDragActive ? '#f0f0f0' : 'transparent',
          minHeight: '40px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          flexDirection: 'column',
        }}
      >
        <input {...getInputProps()} />
        <FontAwesomeIcon icon={faUpload} style={{ marginBottom: '4px' }} />
        <div style={{ fontSize: '12px', color: '#666' }}>{isDragActive ? '拖拽图片到此处' : '点击或拖拽上传图片'}</div>
      </div>

      <Modal isOpen={isModalOpen} toggle={handleCancel} size="lg">
        <ModalHeader toggle={handleCancel}>
          <FontAwesomeIcon icon={faCrop} style={{ marginRight: '8px' }} />
          裁剪图片
        </ModalHeader>
        <ModalBody>
          <div style={{ position: 'relative', height: '400px', width: '100%' }}>
            <Cropper
              image={imageSrc}
              crop={crop}
              zoom={zoom}
              aspect={aspectRatio}
              onCropChange={setCrop}
              onCropComplete={onCropComplete}
              onZoomChange={setZoom}
            />
          </div>
          <div style={{ marginTop: '16px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <div>
              <label style={{ marginRight: '8px' }}>形状比例:</label>
              <select
                value={aspectRatio === undefined ? 'free' : aspectRatio.toString()}
                onChange={e => {
                  const value = e.target.value;
                  if (value === 'free') {
                    setAspectRatio(undefined);
                  } else if (value.includes('/')) {
                    const [numerator, denominator] = value.split('/').map(Number);
                    setAspectRatio(numerator / denominator);
                  } else {
                    setAspectRatio(Number(value));
                  }
                }}
                style={{ padding: '4px 8px', borderRadius: '4px', border: '1px solid #ccc' }}
              >
                <option value="free">自由形状</option>
                <option value="1">正方形 (1:1)</option>
                <option value="4/3">横向 (4:3)</option>
                <option value="3/4">竖向 (3:4)</option>
                <option value="16/9">宽屏 (16:9)</option>
                <option value="9/16">竖屏 (9:16)</option>
                <option value="3/2">横向 (3:2)</option>
                <option value="2/3">竖向 (2:3)</option>
              </select>
            </div>
            <div>
              <label style={{ marginRight: '8px' }}>缩放: {zoom.toFixed(1)}x</label>
              <input
                type="range"
                min="1"
                max="10"
                step="0.1"
                value={zoom}
                onChange={e => setZoom(Number(e.target.value))}
                style={{ width: '200px' }}
              />
            </div>
          </div>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleCancel} disabled={isUploading}>
            <FontAwesomeIcon icon={faTimes} style={{ marginRight: '4px' }} />
            取消
          </Button>
          <Button color="primary" onClick={handleCropAndUpload} disabled={isUploading}>
            <FontAwesomeIcon icon={faCheck} style={{ marginRight: '4px' }} />
            {isUploading ? '上传中...' : '确认上传'}
          </Button>
        </ModalFooter>
      </Modal>
    </>
  );
};

export default ImageUpload;
