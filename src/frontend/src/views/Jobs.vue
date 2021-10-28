<template>
  <v-data-table
      :headers="headers"
      :items="desserts"
      sort-by="calories"
      class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Jobs</v-toolbar-title>
        <v-divider
            class="mx-4"
            inset
            vertical
        ></v-divider>
        <v-icon color="primary"  class="sync-alt" @click="initialize"> reload </v-icon>
        <v-spacer ></v-spacer>
        <!-- dialog 시작 -->
        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                color="primary"
                dark
                v-bind="attrs"
                v-on="on"
                @click="dlg">New Job</v-btn>
          </template>

          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="12" sm="6" md="4">
                    <v-select :items="classes" v-model="editedItem.jobName" label="Job Class">
                    </v-select>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="editedItem.jobGroup" label="Job group"></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-text-field v-model="editedItem.cronExpression" label="Cron Expression"></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="save">Save</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <!-- dialog 종료 -->
      </v-toolbar>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon
          small
          class="mr-2"
          @click="resumeItem(item)">
        mdi-pencil
      </v-icon>
      /
      <v-icon
          small
          @click="stopItem(item)">
        mdi-delete
      </v-icon>

    </template>

    <template v-slot:item.re-actions="{ item }">
      <v-icon
          small
          class="mr-2"
          @click="editItem(item)">
        mdi-pencil
      </v-icon>
      /
      <v-icon
          small
          @click="deleteItem(item)">
        mdi-delete
      </v-icon>
    </template>
  </v-data-table>
</template>

<script>
import axios from 'axios';
export default {
  data: () => ({
    dialog: false,
    headers: [
      {
        text: 'Job Key',
        align: 'start',
        sortable: false,
        value: 'jobKey'
      },
      { text: 'Job 그룹', value: 'jobGroup' },
      { text: 'job 클래스명', value: 'jobClass' },
      { text: '지난 실행일', value: 'lastFiredTime' },
      { text: '다음 실행일', value: 'nextFiredTime' },
      { text: 'Cron 표현식', value: 'cronExpression' },
      { text: '상태' , value : 'jobStatus'},
      { text: '재시작  /  중단', value: 'actions', sortable: false },
      { text: '편집  /  삭제', value: 're-actions', sortable: false }
    ],
    desserts: [],
    editedIndex: -1,
    classes : [],
    editedItem: {
      jobGroup: '',
      jobName: '',
      cronExpression: 0,
      parameters: {
      }
    },
    defaultItem: {
      name: '',
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0
    }
  }),

  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'New Job' : 'Edit Job'
    }
  },

  watch: {
    dialog (val) {
      val || this.close()
    }
  },

  created () {
    this.initialize()
  },

  methods: {
    initialize () {
      axios.get("http://localhost:8087/schedulers/jobs", {
        headers : {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json; charset = utf-8'
        }
      }).then(response =>{
        this.desserts = response.data;
        console.log(response.data);
      }).catch()

    },
    editItem(item) {
      this.editedIndex = this.desserts.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },
    deleteItem(item) {
      const index = this.desserts.indexOf(item)
      confirm('정말로 삭제 하시겠습니까??') && this.desserts.splice(index, 1) &&
      axios.delete("http://localhost:8087/schedulers/job", {jobKey : item.jobKey, jobName : item.jobName, jobGroup : item.jobGroup})
          .then(response => {
            this.initialize();
            console.log(response)
          })
    },resumeItem(item) {
      confirm('재시작 하겠습니까?') &&
      axios.put("http://localhost:8087/schedulers/resume", {jobKey : item.jobKey, jobName : item.jobName, jobGroup : item.jobGroup})
          .then(response => {
            this.initialize();
            console.log(response)
          })
    },stopItem(item) {
      confirm('중단하시겠습니까?') &&
      axios.put("http://localhost:8087/schedulers/pause", {jobKey : item.jobKey, jobName : item.jobName, jobGroup : item.jobGroup})
          .then(response => {
            this.initialize();
            console.log(response)
          })
    },startNowJob(item) {
      confirm('바로 시작하겠습니까?') &&
      axios.put("http://localhost:8087/schedulers/now", {jobKey : item.jobKey, jobName : item.jobName, jobGroup : item.jobGroup})
          .then(response => {
            console.log(response)
          })
    },
    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },
    save () {
      if (this.editedIndex > -1) {
        Object.assign(this.desserts[this.editedIndex], this.editedItem)
      } else {
        this.desserts.push(this.editedItem)
      }
      axios.put("http://localhost:8087/schedulers/job", {
        jobClass : this.editedItem.jobName, jobGroup : this.editedItem.jobGroup, cronExpression : this.editedItem.cronExpression, cronJob : true
      }).then(response =>{
        this.desserts = response.data;
      }).catch()
      this.close()
      this.initialize();
    },
    dlg () {
      axios.get("http://localhost:8087/schedulers/classes", {
        headers : {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json; charset = utf-8'
        }
      }).then(response =>{
        this.classes = response.data;
        console.log(response.data);
      }).catch()
    }
  }
}
</script>
