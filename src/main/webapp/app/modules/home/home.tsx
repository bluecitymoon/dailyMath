import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { Alert, Col, Row, Card, CardBody, CardHeader, CardTitle } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { menuGroups, MenuItem } from 'app/config/menu-config';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = useAppSelector(state => state.authentication.isAdmin);

  const renderMenuItem = (item: MenuItem) => {
    const IconComponent = item.icon as React.ComponentType;
    const isFontAwesomeIcon = typeof item.icon === 'string';

    return (
      <Col md="6" lg="4" xl="3" key={item.id} className="mb-3">
        <Card className="h-100 menu-item-card">
          <CardBody className="text-center">
            <div className="menu-item-icon mb-3">
              {isFontAwesomeIcon ? <FontAwesomeIcon icon={item.icon as any} className="text-primary fa-60px" /> : <IconComponent />}
            </div>
            <h5 className="card-title">{item.title}</h5>
            {item.description && <p className="card-text text-muted small">{item.description}</p>}
            <Link to={item.to} className="btn btn-primary btn-sm mt-2">
              进入
            </Link>
          </CardBody>
        </Card>
      </Col>
    );
  };

  const renderMenuGroup = (group: (typeof menuGroups)[0]) => {
    // 根据用户权限过滤菜单项
    const filteredItems = group.items.filter(item => {
      if (group.id === 'admin' && !isAdmin) {
        return false;
      }
      return true;
    });

    if (filteredItems.length === 0) {
      return null;
    }

    const GroupIcon = group.icon as React.ComponentType;
    const isFontAwesomeIcon = typeof group.icon === 'string';

    return (
      <Col md="12" key={group.id} className="mb-4">
        <Card>
          <CardHeader className="bg-light">
            <CardTitle className="mb-0 d-flex align-items-center">
              <div className="me-3">
                {isFontAwesomeIcon ? <FontAwesomeIcon icon={group.icon as any} className="text-primary" /> : <GroupIcon />}
              </div>
              <span>{group.title}</span>
            </CardTitle>
          </CardHeader>
          <CardBody>
            <Row>{filteredItems.map(renderMenuItem)}</Row>
          </CardBody>
        </Card>
      </Col>
    );
  };

  return (
    <div className="home-container">
      <Row className="mb-4">
        <Col md="12">
          <div className="bg-primary text-light text-center py-4">
            <h1 className="mb-3">欢迎, {account?.login}</h1>
          </div>
        </Col>
      </Row>

      <Row>{menuGroups.map(renderMenuGroup)}</Row>
    </div>
  );
};

export default Home;
