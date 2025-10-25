# File Upload API Documentation

This document describes the file upload API endpoints that have been added to the Daily Math application.

## Overview

The file upload API allows users to upload files with custom folder structure, filename, and business type parameters. Files are stored in a configurable directory structure and can be downloaded or viewed through dedicated endpoints.

## Configuration

File upload settings can be configured in the application properties:

```yaml
application:
  file-storage:
    upload-path: uploads                    # Base upload directory
    max-file-size: 10485760                # Maximum file size in bytes (10MB)
    allowed-extensions: jpg,jpeg,png,gif,pdf,doc,docx,txt,xls,xlsx  # Allowed file extensions
    base-url: http://localhost:8080/api/files  # Base URL for file access
```

## API Endpoints

### 1. Upload File

**POST** `/api/files/upload`

Upload a file with custom parameters.

**Parameters:**
- `file` (MultipartFile): The file to upload
- `folder` (String): The folder to store the file
- `filename` (String): The custom filename
- `bizType` (String): The business type

**Example Request:**
```bash
curl -X POST "http://localhost:8080/api/files/upload" \
  -F "file=@document.pdf" \
  -F "folder=documents" \
  -F "filename=my-document" \
  -F "bizType=contract"
```

**Response:**
```json
{
  "originalFilename": "document.pdf",
  "storedFilename": "my-document_a1b2c3d4.pdf",
  "folder": "documents",
  "bizType": "contract",
  "filePath": "uploads/documents/contract/my-document_a1b2c3d4.pdf",
  "fileSize": 1024000,
  "contentType": "application/pdf",
  "uploadTime": "2024-01-15T10:30:00Z",
  "downloadUrl": "http://localhost:8080/api/files/download?filePath=documents/contract/my-document_a1b2c3d4.pdf"
}
```

### 2. Upload File with DTO

**POST** `/api/files/upload-with-dto`

Upload a file using a DTO for parameters (alternative endpoint).

**Parameters:**
- `file` (MultipartFile): The file to upload
- `requestDTO` (FileUploadRequestDTO): Object containing folder, filename, and bizType

### 3. Get Download URL

**GET** `/api/files/download-url`

Get the download URL for a file.

**Parameters:**
- `filePath` (String): The file path

**Example Request:**
```bash
curl "http://localhost:8080/api/files/download-url?filePath=documents/contract/my-document_a1b2c3d4.pdf"
```

**Response:**
```
http://localhost:8080/api/files/download?filePath=documents/contract/my-document_a1b2c3d4.pdf
```

### 4. Download File

**GET** `/api/files/download?filePath={filePath}`

Download a file by its path.

**Example Request:**
```bash
curl "http://localhost:8080/api/files/download?filePath=documents/contract/my-document_a1b2c3d4.pdf" -o downloaded-file.pdf
```

### 5. View File

**GET** `/api/files/view?filePath={filePath}`

View a file inline in the browser.

**Example Request:**
```bash
curl "http://localhost:8080/api/files/view?filePath=documents/contract/my-document_a1b2c3d4.pdf"
```

### 6. Delete File

**DELETE** `/api/files?filePath={filePath}`

Delete a file by its path.

**Example Request:**
```bash
curl -X DELETE "http://localhost:8080/api/files?filePath=documents/contract/my-document_a1b2c3d4.pdf"
```

## File Storage Structure

Files are stored in the following directory structure:
```
{upload-path}/
  {folder}/
    {bizType}/
      {filename}_{uniqueId}.{extension}
```

Example:
```
uploads/
  documents/
    contract/
      my-document_a1b2c3d4.pdf
  images/
    profile/
      avatar_b2c3d4e5.jpg
```

## Security Features

1. **File Type Validation**: Only allowed file extensions are accepted
2. **File Size Limits**: Maximum file size is enforced
3. **Path Traversal Protection**: Files can only be accessed within the upload directory
4. **Unique Filenames**: Generated unique identifiers prevent filename conflicts

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK`: Successful operation
- `400 Bad Request`: Invalid request (empty file, validation errors)
- `404 Not Found`: File not found
- `403 Forbidden`: Access denied (path traversal attempt)
- `500 Internal Server Error`: Server error during file operations

## DTOs

### FileUploadRequestDTO
```java
{
  "folder": "string",      // Required: Target folder
  "filename": "string",    // Required: Custom filename
  "bizType": "string"      // Required: Business type
}
```

### FileUploadResponseDTO
```java
{
  "originalFilename": "string",    // Original uploaded filename
  "storedFilename": "string",      // Generated unique filename
  "folder": "string",              // Target folder
  "bizType": "string",             // Business type
  "filePath": "string",            // Full file path
  "fileSize": "number",            // File size in bytes
  "contentType": "string",         // MIME type
  "uploadTime": "datetime",        // Upload timestamp
  "downloadUrl": "string"          // Download URL
}
```

## Usage Examples

### Frontend Integration (JavaScript)

```javascript
// Upload file
const formData = new FormData();
formData.append('file', fileInput.files[0]);
formData.append('folder', 'documents');
formData.append('filename', 'my-document');
formData.append('bizType', 'contract');

fetch('/api/files/upload', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => {
  console.log('File uploaded:', data);
  // Use data.downloadUrl to access the file
});
```

### Java Client Example

```java
@Service
public class FileUploadClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public FileUploadResponseDTO uploadFile(MultipartFile file, String folder, String filename, String bizType) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file);
        body.add("folder", folder);
        body.add("filename", filename);
        body.add("bizType", bizType);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        
        return restTemplate.postForObject("/api/files/upload", requestEntity, FileUploadResponseDTO.class);
    }
}
```
