import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface EmptyStateMessageProps {
  message: string;
  loading?: boolean;
}

export const EmptyStateMessage: React.FC<EmptyStateMessageProps> = ({ message, loading = false }) => {
  if (loading) {
    return null;
  }

  return (
    <div
      className="alert alert-info text-center"
      style={{
        fontSize: '1.1rem',
        fontWeight: '500',
        padding: '1.5rem',
        border: '2px solid var(--tblr-info)',
        backgroundColor: 'var(--tblr-info-lt)',
        color: 'var(--tblr-info)',
      }}
    >
      <FontAwesomeIcon icon="info-circle" className="me-2" />
      {message}
    </div>
  );
};

export default EmptyStateMessage;
