<template lang="">
  <ContentField>
    <div class="container">
      <div class="row">
        <div class="col-3">
          <div class="card" style="margin-top: 20px">
            <div class="card-body">
              <img :src="$store.state.user.photo" alt="" style="width: 100%" />
            </div>
          </div>
        </div>
        <div class="col-9">
          <div class="card" style="margin-top: 20px">
            <div class="card-header">
              <span style="font-size: 130%">我的Bot</span>
              <button
                type="button"
                class="btn btn-primary float-end"
                data-bs-toggle="modal"
                data-bs-target="#add-bot-modal"
              >
                创建Bot
              </button>
              <!-- 增加一个模态框 -->
              <div
                class="modal fade"
                id="add-bot-modal"
                tabindex="-1"
                aria-labelledby="add-bot-modal-label"
                aria-hidden="true"
              >
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="add-bot-modal-label">
                        创建 Bot
                      </h5>
                      <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                      ></button>
                    </div>
                    <div class="modal-body">
                      <!-- Bot 名称输入 -->
                      <div class="mb-3">
                        <label for="add-bot-title" class="form-label"
                          >名称</label
                        >
                        <input
                          type="text"
                          class="form-control"
                          id="add-bot-title"
                          v-model="addBotData.title"
                          placeholder="请输入 Bot 名称"
                        />
                      </div>
                      <!-- Bot 简介输入 -->
                      <div class="mb-3">
                        <label for="add-bot-description" class="form-label"
                          >简介</label
                        >
                        <textarea
                          class="form-control"
                          id="add-bot-description"
                          rows="3"
                          v-model="addBotData.description"
                          placeholder="请输入 Bot 简介"
                        ></textarea>
                      </div>
                      <!-- Bot 代码输入 -->
                      <div class="mb-3">
                        <label for="add-bot-content" class="form-label"
                          >代码</label
                        >
                        <textarea
                          class="form-control"
                          id="add-bot-content"
                          rows="5"
                          v-model="addBotData.content"
                          placeholder="请输入 Bot 代码"
                        ></textarea>
                      </div>
                    </div>
                    <div class="modal-footer">
                      <!-- 错误信息显示 -->
                      <div
                        class="alert alert-danger fade show"
                        role="alert"
                        v-if="addBotData.error_message"
                      >
                        {{ addBotData.error_message }}
                      </div>
                      <!-- 创建按钮 -->
                      <button
                        type="button"
                        class="btn btn-primary"
                        @click="addBot(addBotData)"
                      >
                        创建
                      </button>
                      <!-- 取消按钮 -->
                      <button
                        type="button"
                        class="btn btn-secondary"
                        data-bs-dismiss="modal"
                      >
                        取消
                      </button>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 创建成功提示 -->
              <div
                class="modal fade"
                id="successModal"
                tabindex="-1"
                aria-hidden="true"
              >
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title">操作成功</h5>
                      <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                      ></button>
                    </div>
                    <div class="modal-body">
                      {{ successMessage }}
                    </div>
                    <div class="modal-footer">
                      <button
                        type="button"
                        class="btn btn-success"
                        data-bs-dismiss="modal"
                      >
                        确定
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card-body">
              <!-- Bootstrap的响应式表格 -->
              <table class="table">
                <!-- 表头 -->
                <thead class="thead-light">
                  <!-- 添加thead-light类为表头添加浅色背景 -->
                  <tr>
                    <th scope="col">名称</th>
                    <th scope="col">描述</th>
                    <th scope="col">创建时间</th>
                    <th scope="col">操作</th>
                  </tr>
                </thead>
                <!-- 表体 -->
                <tbody>
                  <tr v-for="bot in bots" :key="bot.id">
                    <td>{{ bot.title }}</td>
                    <td>{{ bot.description }}</td>
                    <td>{{ bot.createTime }}</td>
                    <td>
                      <!-- 添加按钮类，移除内联样式 -->
                      <button type="button" class="btn btn-secondary me-2">
                        修改
                      </button>
                      <!-- Bootstrap 4使用mr-2，Bootstrap 5使用me-2 -->
                      <button type="button" class="btn btn-danger">删除</button>
                    </td>
                  </tr>
                </tbody>
              </table>

              <!--分页控件-->
              <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                  <!-- Previous Page Link -->
                  <li
                    class="page-item"
                    :class="{ disabled: currentPage === 1 }"
                  >
                    <a
                      class="page-link"
                      href="#"
                      aria-label="Previous"
                      @click.prevent="gotoPage(currentPage - 1)"
                    >
                      <span aria-hidden="true">&laquo;</span>
                    </a>
                  </li>
                  <!-- 中间的页码 -->
                  <li
                    v-for="page in paginatedPages"
                    :key="page"
                    class="page-item"
                    :class="{
                      active: currentPage === page,
                      disabled: page === '...',
                    }"
                  >
                    <a
                      class="page-link"
                      href="#"
                      @click.prevent="page !== '...' && gotoPage(page)"
                    >
                      {{ page }}
                    </a>
                  </li>

                  <!-- Next Page Link -->
                  <li
                    class="page-item"
                    :class="{ disabled: currentPage === totalPages }"
                  >
                    <a
                      class="page-link"
                      href="#"
                      aria-label="Next"
                      @click.prevent="gotoPage(currentPage + 1)"
                    >
                      <span aria-hidden="true">&raquo;</span>
                    </a>
                  </li>
                </ul>
                <!-- Items Per Page Dropdown -->
                <div class="dropdown ml-3">
                  <button
                    class="btn btn-secondary dropdown-toggle"
                    type="button"
                    id="dropdownMenuButton"
                    data-bs-toggle="dropdown"
                    aria-haspopup="true"
                    aria-expanded="false"
                  >
                    {{ pageSize }}
                  </button>
                  <div
                    class="dropdown-menu"
                    aria-labelledby="dropdownMenuButton"
                  >
                    <a
                      class="dropdown-item"
                      href="#"
                      @click.prevent="changePageSize(2)"
                      >2</a
                    >
                    <a
                      class="dropdown-item"
                      href="#"
                      @click.prevent="changePageSize(3)"
                      >3</a
                    >
                    <a
                      class="dropdown-item"
                      href="#"
                      @click.prevent="changePageSize(4)"
                      >4</a
                    >
                  </div>
                </div>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>
  </ContentField>
</template>
<script>
import ContentField from "@/components/ContentField";
import { ref, computed, reactive } from "vue";
import axios from "axios";
import { useStore } from "vuex";
import * as bootstrap from "bootstrap";
export default {
  components: {
    ContentField,
  },
  setup() {
    const bots = ref();
    const currentPage = ref(1); //当前页
    const pageSize = ref(2); //每页显示几个数据
    const totalBots = ref(0); //数据总数
    const totalPages = computed(() =>
      Math.ceil(totalBots.value / pageSize.value)
    ); //总页数
    //动态显示分页页码
    const paginatedPages = computed(() => {
      let pages = [];
      let startPage, endPage;

      if (totalPages.value <= 10) {
        // 总页数小于等于10，显示所有页码
        startPage = 1;
        endPage = totalPages.value;
      } else {
        // 总页数大于10，计算当前的动态范围
        if (currentPage.value <= 6) {
          startPage = 1;
          endPage = 10;
        } else if (currentPage.value + 4 >= totalPages.value) {
          startPage = totalPages.value - 9;
          endPage = totalPages.value;
        } else {
          startPage = currentPage.value - 5;
          endPage = currentPage.value + 4;
        }
      }

      // 添加开始的省略号
      if (startPage > 1) {
        pages.push(1);
        pages.push("...");
      }

      // 添加所有的中间页码
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }

      // 添加结束的省略号
      if (endPage < totalPages.value) {
        pages.push("...");
        pages.push(totalPages.value);
      }

      return pages;
    });
    // 跳转到指定页码
    const gotoPage = (pageNumber) => {
      if (pageNumber < 1 || pageNumber > totalPages.value) {
        return;
      }
      currentPage.value = pageNumber;
      getList();
    };

    const store = useStore();
    let base_url = "http://127.0.0.1:3000/user/bot/";
    const getList = () => {
      axios
        .get(base_url + "getList", {
          params: {
            page: currentPage.value,
            limit: pageSize.value,
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token, // 请求头中包含 token
          },
        })
        .then((response) => {
          const responseData = response.data;
          console.log("获取bot列表成功: " + JSON.stringify(responseData));
          bots.value = responseData.records; //获取到的分页bot数组
          totalBots.value = responseData.total; //Bot总数
        })
        .catch((error) => {
          console.log("获取bot列表失败: " + JSON.stringify(error));
        });
    };

    //更改每页显示的条数
    const changePageSize = (newPageSize) => {
      pageSize.value = newPageSize;
      currentPage.value = 1; // Reset to first page
      getList(); // Method to load data based on new limit
    };

    //reactive 用于创建一个响应式的复杂类型的数据，比如对象或数组。
    const addBotData = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });
    //添加机器人时的数据校验
    const validateInput = (data) => {
      // Reset error message before validation
      data.error_message = "";

      // Validate title
      if (!data.title) {
        data.error_message += "标题不能为空";
      } else if (data.title.length > 100) {
        data.error_message += "标题长度不能大于100";
      }

      // Validate description
      if (data.description.length > 100) {
        data.error_message += "简介长度不能超过100";
      }

      // Validate content
      if (!data.content) {
        data.error_message += "代码不能为空。";
      } else if (data.content.length > 1000000) {
        data.error_message += "代码长度不能超过1000000。";
      }

      // If error_message is empty, validation passed
      return data.error_message === "";
    };

    const addBot = (data) => {
      if (!validateInput(data)) {
        setTimeout(() => (addBotData.error_message = ""), 1500);
        return;
      }
      axios
        .post(
          base_url + "add",
          {
            title: data.title,
            description: data.description,
            content: data.content,
          },
          {
            headers: {
              Authorization: "Bearer " + store.state.user.token, // 请求头中包含 token
            },
          }
        )
        .then((response) => {
          const responseData = response.data;
          data.error_message = responseData.error_message;
          if (responseData.error_message == "success") {
            document.querySelector('button[data-bs-dismiss="modal"]').click();
            new bootstrap.Modal(document.getElementById("successModal")).show();
            getList();
          } else {
            console.log("添加bot失败: " + JSON.stringify(responseData));
          }
        })
        .catch((error) => {
          data.error_message = error;
          console.log("添加bot失败: " + JSON.stringify(error));
        });
      setTimeout(() => (addBotData.error_message = ""), 1500);
    };

    getList();
    return {
      bots,
      currentPage,
      totalPages,
      pageSize,
      gotoPage,
      getList,
      addBot,
      addBotData,
      changePageSize,
      paginatedPages,
    };
  },
};
</script>
<style lang=""></style>
