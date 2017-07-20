INSERT INTO invoice_user(username, password, last_login_time, last_login_ip, email,
  enabled, account_expired, credentials_expired, account_locked, gmt_create, gmt_modify)
VALUES
  ('admin1','admin1',current_timestamp, '192.168.1.1', '1@qq.com', true, false, false, false, current_timestamp, current_timestamp),
  ('admin2','admin2',current_timestamp, '192.168.1.2', '2@qq.com', true, false, false, false, current_timestamp, current_timestamp),
  ('admin3','admin3',current_timestamp, '192.168.1.3', '3@qq.com', true, false, false, false, current_timestamp, current_timestamp);

INSERT INTO invoice_role(name, description, enabled, gmt_create, gmt_modify)
VALUES
  ('role.root', '最高权限管理员', true, current_timestamp, current_timestamp),
  ('role.operation', '负责公司运营管理', true, current_timestamp, current_timestamp),
  ('role.finance', '负责公司财务管理', true, current_timestamp, current_timestamp);

INSERT INTO invoice_auth(name, description, enabled, gmt_create, gmt_modify)
VALUES
  ('auth.index.view','访问首页', true, current_timestamp, current_timestamp),
  ('auth.user.add','添加用户', true, current_timestamp, current_timestamp),
  ('auth.user.disable','禁用用户', FALSE , current_timestamp, current_timestamp),
  ('auth.user.delete','删除用户', true, current_timestamp, current_timestamp),
  ('auth.user.view.self','查看自己的用户信息', true, current_timestamp, current_timestamp),
  ('auth.user.view.all','查看所有的用户信息', true, current_timestamp, current_timestamp),
  ('auth.user.edit','修改自己的信息', true, current_timestamp, current_timestamp),
  ('auth.user.edit.role','修改用户的角色', true, current_timestamp, current_timestamp),
  ('auth.role.add','添加角色', true, current_timestamp, current_timestamp),
  ('auth.role.disable','禁用角色', true, current_timestamp, current_timestamp),
  ('auth.role.delete','删除角色', true, current_timestamp, current_timestamp),
  ('auth.role.view.all','查看所有的角色信息', true, current_timestamp, current_timestamp),
  ('auth.role.edit','修改角色信息', true, current_timestamp, current_timestamp),
  ('auth.auth.add','添加权限', true, current_timestamp, current_timestamp),
  ('auth.auth.disable','禁用权限', true, current_timestamp, current_timestamp),
  ('auth.auth.delete','删除权限', true, current_timestamp, current_timestamp),
  ('auth.auth.view.all','查看所有权限信息', true, current_timestamp, current_timestamp),
  ('auth.auth.edit','修改权限信息', true, current_timestamp, current_timestamp),
  ('auth.invoice.add','添加发票', true, current_timestamp, current_timestamp),
  ('auth.invoice.disable','禁用发票', true, current_timestamp, current_timestamp),
  ('auth.invoice.delete','删除发票', true, current_timestamp, current_timestamp),
  ('auth.invoice.view.all','查看所有发票信息', true, current_timestamp, current_timestamp),
  ('auth.invoice.edit','编辑发票', true, current_timestamp, current_timestamp),
  ('auth.invoice.review','审查发票', true, current_timestamp, current_timestamp);

INSERT INTO invoice_user_role(user_id, role_id, gmt_create, gmt_modify)
VALUES
  (1, 1, current_timestamp, current_timestamp),
  (1, 2, current_timestamp, current_timestamp);
#   (1, 3, current_timestamp, current_timestamp);

INSERT INTO invoice_role_auth(role_id, auth_id, gmt_create, gmt_modify)
VALUES
  (1, 1, current_timestamp, current_timestamp),
  (1, 2, current_timestamp, current_timestamp),
  (1, 3, current_timestamp, current_timestamp),
  (1, 4, current_timestamp, current_timestamp),
  (1, 5, current_timestamp, current_timestamp),
  (2, 1, current_timestamp, current_timestamp),
  (3, 1, current_timestamp, current_timestamp);