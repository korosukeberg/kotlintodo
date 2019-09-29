<template>
    <v-app>


        <v-content>
            <v-container>
                <v-layout
                        wrap
                >
                    <v-flex xs12>
                        <v-img
                                :src="require('./assets/logo.svg')"
                                class="my-3"
                                contain
                                height="200"
                        ></v-img>
                    </v-flex>

                    <v-flex>
                        <v-layout>
                            <v-text-field
                                    label="Solo"
                                    placeholder="Add your todo here"
                                    solo
                                    v-on:keyup.enter="add"
                                    v-model="newItemTitle"
                            >
                                <template v-slot:append>
                                    <v-btn class="ma-2" tile color="primary" v-on:click="add">ADD</v-btn>
                                </template>
                            </v-text-field>
                        </v-layout>
                        <v-layout justify-end>
                            <v-btn text small color="warning" v-on:click="clearCompleted">CLEAR COMPLETED
                            </v-btn>
                            <v-btn text small color="error" v-on:click="clearAll">CLEAR ALL</v-btn>
                        </v-layout>
                    </v-flex>

                    <v-flex xs12>
                        <v-card max-width="475" class="mx-auto">
                            <v-list subheader two-line flat>
                                <v-list-item-group multiple>
                                    <v-list-item v-for="item in items">
                                        <template>
                                            <v-list-item-action>
                                                <v-checkbox
                                                        v-model="item.isCompleted"
                                                        color="primary"
                                                ></v-checkbox>
                                            </v-list-item-action>

                                            <v-list-item-content>
                                                <v-list-item-title
                                                        v-bind:class="{'primary--text done': item.isCompleted}">{{
                                                    item.title }}
                                                </v-list-item-title>
                                            </v-list-item-content>
                                        </template>
                                    </v-list-item>
                                </v-list-item-group>
                            </v-list>
                        </v-card>
                    </v-flex>

                    <!-- <HelloWorld/> -->
                </v-layout>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import HelloWorld from './components/HelloWorld';
    import axiosBase from "axios"

    const axios = axiosBase.create({
        baseURL: "http://localhost:8081"
    })

    export default {
        name: 'App',
        components: {
            HelloWorld,
        },
        data: function () {
            return {
                newItemTitle: "",
                items: []
            }
        },
        mounted: function () {
            this.load();
        },
        methods: {
            load: function () {
                axios.get("/todo").then((response) => {
                    for (let i = 0; i < response.data.length; i++) {
                        this.items.push(response.data[i])
                    }
                })
            },
            add: function () {
                if (!this.newItemTitle) {
                    return;
                }
                let newItem = {
                    id: this.items.length ? this.items.reduce((a, b) => a.id > b.id ? a : b).id + 1 : 1,
                    title: this.newItemTitle,
                    isCompleted: false
                }
                axios.post("/todo", newItem).then(response => {
                    this.items.push(newItem);
                    this.newItemTitle = "";
                }, error => {
                    console.log(error);
                });
            },
            clearCompleted: function () {
                let completedItems = [];
                for (let i = 0; i < this.items.length; i++) {
                    if (this.items[i].isCompleted) {
                        completedItems.push(this.items[i].id)
                        this.items.splice(i--, 1);
                    }
                }
                if (!completedItems.length) {
                    return
                }
                axios.delete("/todo", {data: completedItems}).then(response => {
                    }
                    , error => {
                        console.log(error);
                    });
            },
            clearAll: function () {
                let itemIds = this.items.map(value => {
                    return value.id
                })
                axios.delete("/todo", {data: itemIds}).then(response => {
                    this.items = [];
                }, error => {
                    console.log(error);
                });
            }
        }
    }
</script>
<style>
    .done {
        text-decoration: line-through;
    }

</style>
