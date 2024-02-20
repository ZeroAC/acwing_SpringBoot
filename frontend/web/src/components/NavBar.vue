<template>
  <nav class="navbar navbar-expand-lg bg-dark navbar-dark">
    <div class="container-fluid">
      <router-link
        class="navbar-brand"
        active-class="active-link"
        :to="{ name: 'home_index' }"
        >King Of Bots</router-link
      >
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarText"
        aria-controls="navbarText"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto ms-5 mb-2 mb-lg-0">
          <li class="nav-item me-3">
            <router-link
              class="nav-link"
              active-class="active-link"
              :to="{ name: 'pk_index' }"
              aria-current="page"
              >对战</router-link
            >
          </li>
          <li class="nav-item me-3">
            <router-link
              class="nav-link"
              active-class="active-link"
              :to="{ name: 'record_index' }"
              aria-current="page"
              >对局列表</router-link
            >
          </li>
          <li class="nav-item me-3">
            <router-link
              class="nav-link"
              active-class="active-link"
              :to="{ name: 'ranklist_index' }"
              aria-current="page"
              >排行榜</router-link
            >
          </li>
        </ul>
        <ul class="navbar-nav mb-2 mb-lg-0 me-5 ms-5">
          <div class="nav-item dropdown">
            <button
              class="btn btn-secondary dropdown-toggle bg-dark"
              type="button"
              id="dropdownMenuButton"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              我的名字
            </button>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <li>
                <router-link
                  class="dropdown-item"
                  active-class="active-link"
                  :to="{ name: 'user_bot_index' }"
                  >我的Bot</router-link
                >
                <router-link
                  class="dropdown-item"
                  active-class="active-link"
                  :to="{ name: 'user_account_login' }"
                  >登录</router-link
                >
                <router-link
                  class="dropdown-item"
                  active-class="active-link"
                  :to="{ name: 'user_account_register' }"
                  >注册</router-link
                >
              </li>
              <li><a class="dropdown-item" href="#">退出</a></li>
            </ul>
          </div>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script>
export default {};
</script>
<style scoped>
.active-link {
  color: rgb(30, 237, 252); /* 设置高亮颜色 */
  font-weight: bold; /* 设置字体加粗 */
}
</style>
