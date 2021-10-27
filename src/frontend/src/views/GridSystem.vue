<template>
  <div>
    <v-container fluid>
      <v-card class="pa-3">
        <v-row :style=" {
            height : '120px' }"
               class="red"
               align="end"
        >
          <v-col>
            <v-card
                class="pa-3"
                outlined>
              Col
            </v-card>
          </v-col>
          <v-col>
            <v-card
                class="pa-3"
                outlined>
              Col
            </v-card>
          </v-col>
          <v-col>
            <v-card
                class="pa-3"
                outlined>
              Col
            </v-card>
          </v-col>
        </v-row>
        <v-row :style="{
            height : '120px' }"
               class="yellow"
        >
          <v-col align-self="end">
            <v-card
                class="pa-3"
                outlined>
              Col
            </v-card>
          </v-col>
          <v-col>
            <v-card
                class="pa-3"
                outlined>
              Col
            </v-card>
          </v-col>
          <v-col>
            <v-card
                class="pa-3"
                outlined>
              Col
            </v-card>
          </v-col>
        </v-row>
      </v-card>
    </v-container>
  </div>
</template>

<script>
export default {

}
</script>

<style>

</style>