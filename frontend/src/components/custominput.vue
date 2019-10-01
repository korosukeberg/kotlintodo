<template>
  <v-text-field solo :placeholder="myplaceholder" class="my-4">
    <template v-slot:append>
      <v-btn tile color="primary">
        <slot name="buttonlabel">ADD</slot>
      </v-btn>
    </template>
  </v-text-field>
</template>

<script>
  export default {
    data: () => ({}),
    props: {
      myplaceholder: String
    }
  };
</script>
