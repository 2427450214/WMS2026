// 表单验证工具函数

// 验证登录表单
export const validateLoginForm = (form) => {
  // 账号校验
  if (!form.account) {
    return { valid: false, message: '账号不能为空' }
  }
  if (!/^\d{8,12}$/.test(form.account)) {
    return { valid: false, message: '账号只能为8到12位纯数字' }
  }
  
  // 密码校验 - 登录时只校验是否为空，不校验格式
  if (!form.password) {
    return { valid: false, message: '密码不能为空' }
  }
  
  return { valid: true, message: '' }
}

// 验证注册表单
export const validateRegisterForm = (form) => {
  // 账号校验
  if (!form.account) {
    return { valid: false, message: '账号不能为空' }
  }
  if (!/^\d{8,12}$/.test(form.account)) {
    return { valid: false, message: '账号只能为8到12位纯数字' }
  }
  
  // 姓名校验
  if (!form.name) {
    return { valid: false, message: '姓名不能为空' }
  }
  if (!/^[\u4e00-\u9fa5a-zA-Z]{2,10}$/.test(form.name)) {
    return { valid: false, message: '姓名只能是二到10位的大小写英文或者纯中文' }
  }
  
  // 密码校验
  if (!form.password) {
    return { valid: false, message: '密码不能为空' }
  }
  if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/.test(form.password)) {
    return { valid: false, message: '密码必须为8到16位大小写英文加数字加特殊字符的组合' }
  }
  
  // 确认密码校验
  if (!form.confirmPassword) {
    return { valid: false, message: '请确认密码' }
  }
  if (form.password !== form.confirmPassword) {
    return { valid: false, message: '两次输入的密码不一致' }
  }
  
  return { valid: true, message: '' }
}
