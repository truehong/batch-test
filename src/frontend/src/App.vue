<template>
  <v-app>
    <v-app-bar
        app
        color="grey darken-3"
        dark
    >
      <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      <v-spacer/>
    </v-app-bar>
    <v-navigation-drawer v-model="drawer" app >
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="text-h6">
             Quartz Admin
          </v-list-item-title>
          <v-list-item-subtitle>
            for member jobs
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list
          dense
          nav
      >
        <v-list-item
            v-for="item in items"
            :key="item.title"
            link
            :to="item.to"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-main>
      <v-container fluid>
        <router-view/>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>

export default {
  name: 'App',

  data: () => ({
    drawer : false,
    items: [
      { title: 'Dashboard', icon: 'mdi-view-dashboard', to: '/' },
      { title: 'JOB', icon: 'mdi-clock-plus' , to: '/jobs'},
      { title: 'History', icon: 'mdi-calendar-text' , to: '/history'},
      { title: 'Setting', icon: 'mdi-cog-outline' , to: '/tmp'}
    ],
    right: null,
  }),
};
</script>
