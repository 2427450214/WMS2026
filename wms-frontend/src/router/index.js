import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/admin',
    name: 'AdminHome',
    component: () => import('../views/AdminHomeView.vue'),
    meta: { requiresAuth: true, roles: ['管理员'] },
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: null,
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'user-list',
        name: 'UserList',
        component: () => import('../views/UserListView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'permission-manage',
        name: 'PermissionManage',
        component: () => import('../views/PermissionManageView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'goods-manage',
        name: 'AdminGoodsManage',
        component: () => import('../views/GoodsManageView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'goods-category-manage',
        name: 'GoodsCategoryManage',
        component: () => import('../views/GoodsCategoryManageView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'batch-manage',
        name: 'BatchManage',
        component: () => import('../views/BatchManageView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'warehouse-manage',
        name: 'AdminWarehouseManage',
        component: () => import('../views/WarehouseManageView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'inbound-manage',
        name: 'AdminInboundManage',
        component: () => import('../views/InboundManageView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'outbound-manage',
        name: 'AdminOutboundManage',
        component: () => import('../views/OutboundManageView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'in-out-log',
        name: 'AdminInOutLog',
        component: () => import('../views/InOutLogView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'goods-data',
        name: 'AdminGoodsData',
        component: () => import('../views/GoodsDataView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'warehouse-data',
        name: 'AdminWarehouseData',
        component: () => import('../views/WarehouseDataView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'operation-log',
        name: 'AdminOperationLog',
        component: () => import('../views/OperationLogView.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: null,
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'settings',
        name: 'AdminSettings',
        component: null,
        meta: { requiresAuth: true, roles: ['管理员'] }
      }
    ]
  },
  {
    path: '/user',
    name: 'UserHome',
    component: () => import('../views/UserHomeView.vue'),
    meta: { requiresAuth: true, roles: ['user', '管理员'] },
    redirect: '/user/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'UserDashboard',
        component: null,
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      },
      {
        path: 'goods-manage',
        name: 'UserGoodsManage',
        component: () => import('../views/GoodsManageView.vue'),
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      },
      {
        path: 'goods-category-manage',
        name: 'UserGoodsCategoryManage',
        component: () => import('../views/GoodsCategoryManageView.vue'),
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      },
      {
        path: 'warehouse-manage',
        name: 'UserWarehouseManage',
        component: () => import('../views/WarehouseManageView.vue'),
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      },
      {
        path: 'in-out-log',
        name: 'UserInOutLog',
        component: () => import('../views/InOutLogView.vue'),
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      },
      {
        path: 'goods-data',
        name: 'UserGoodsData',
        component: () => import('../views/GoodsDataView.vue'),
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      },
      {
        path: 'warehouse-data',
        name: 'UserWarehouseData',
        component: () => import('../views/WarehouseDataView.vue'),
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: null,
        meta: { requiresAuth: true, roles: ['user', '管理员'] }
      }
    ]
  },
  {
    path: '/admin/test',
    name: 'TestAllComponents',
    component: () => import('../views/TestAllComponents.vue'),
    meta: { requiresAuth: true, roles: ['管理员'] }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  const userRole = localStorage.getItem('userRole')

  if (to.meta.requiresAuth && !isLoggedIn) {
    // 需要认证但未登录，跳转到登录页
    next('/login')
  } else if (to.meta.requiresAuth && isLoggedIn && to.meta.roles) {
    // 需要认证且已登录，验证用户角色
    if (to.meta.roles.includes(userRole)) {
      // 角色匹配，允许访问
      next()
    } else {
      // 角色不匹配，跳转到对应角色的主页
      if (userRole === '管理员') {
        next('/admin')
      } else {
        next('/user')
      }
    }
  } else if (to.path === '/login' && isLoggedIn) {
    // 已登录用户访问登录页，跳转到对应主页
    if (userRole === '管理员') {
      next('/admin')
    } else {
      next('/user')
    }
  } else {
    next()
  }
})

export default router