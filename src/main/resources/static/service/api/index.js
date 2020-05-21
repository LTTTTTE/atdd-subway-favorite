const METHOD = {
  PUT() {
    return {
      method: 'PUT'
    }
  },
  DELETE() {
    return {
      method: 'DELETE'
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  GET(token) {
    return {
      method: 'GET',
      headers: {
        'authorization': 'bearer ' + token,
        'Content-Type': 'application/json'
      }
    }
  }
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

  const line = {
    getAll() {
      return request(`/lines/detail`)
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`)
    }
  }

  const path = {
    find(params) {
      return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  }

  const member = {
    create(data) {
      return request('/members', METHOD.POST(data));
    },
    login(data) {
      return fetch('/oauth/token', METHOD.POST(data));
    },
    get(token) {
      return requestWithJsonData('/members/me', METHOD.GET(token));
    }
  }

  return {
    line,
    path,
    member
  }
})()

export default api
