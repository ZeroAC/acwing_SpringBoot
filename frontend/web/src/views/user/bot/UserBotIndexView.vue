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
              <button type="button" class="btn btn-primary float-end">
                创建Bot
              </button>
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

                  <!-- Page Numbers -->
                  <!-- <li
                    v-for="page in totalPages"
                    :key="page"
                    class="page-item"
                    :class="{ active: currentPage === page }"
                  >
                    <a
                      class="page-link"
                      href="#"
                      @click.prevent="gotoPage(page)"
                    >
                      {{ page }}
                    </a>
                  </li> -->
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
import { ref, computed } from "vue";
import axios from "axios";
import { useStore } from "vuex";
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

    const changePageSize = (newPageSize) => {
      pageSize.value = newPageSize;
      currentPage.value = 1; // Reset to first page
      getList(); // Method to load data based on new limit
    };

    const addBot = (data) => {
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
          if (responseData.error_message == "success") {
            getList();
          } else {
            console.log("添加bot失败: " + JSON.stringify(responseData));
          }
        })
        .catch((error) => {
          console.log("添加bot失败: " + JSON.stringify(error));
        });
    };
    addBot({
      title: "第二个bot",
      description: "第二个bot的描述",
      content: "第二个bot的内容",
    });
    getList();
    return {
      bots,
      currentPage,
      totalPages,
      pageSize,
      gotoPage,
      getList,
      addBot,
      changePageSize,
      paginatedPages,
    };
  },
};
</script>
<style lang=""></style>
