import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';

export interface IFa6MenuItem {
  children: React.ReactNode;
  icon: React.ComponentType;
  to: string;
  id?: string;
  'data-cy'?: string;
}

const Fa6MenuItem = (props: IFa6MenuItem) => {
  const { to, icon: Icon, id, children } = props;

  return (
    <DropdownItem tag={Link} to={to} id={id} data-cy={props['data-cy']}>
      <span style={{ display: 'inline-flex', alignItems: 'center', marginRight: '8px', fontSize: '16px' }}>
        <Icon />
      </span>
      {children}
    </DropdownItem>
  );
};

export default Fa6MenuItem;
