package com.example.musicapp.Model;

public class Item_CaTop {

        private Category category;
        private Topic topic;

        public Item_CaTop(Category category, Topic topic) {
            this.category = category;
            this.topic = topic;
        }

        public Category getCategory() {
            return category;
        }

        public Topic getTopic() {
            return topic;
        }

}
