
import request from './request'

export async function getHomepageComponents() {
  return await request('/system/settings/homepage', {
    method: 'GET'
  })
}

export async function setHomepageComponents(components) {
  return await request('/system/settings/homepage', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(components)
  })
}

export async function resetHomepageComponents() {
  return await request('/system/settings/homepage/reset', {
    method: 'POST'
  })
}
