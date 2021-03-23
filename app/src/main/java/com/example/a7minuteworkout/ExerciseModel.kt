package com.example.a7minuteworkout

class ExerciseModel(private var id: Int,
                    private var name: String,
                    private var image: Int,
                    private var isCompleted: Boolean,
                    private var isSelected: Boolean)
{
    public fun setId(id: Int){
        this.id = id
    }

    public fun getId(): Int{
        return this.id
    }

    public fun setName(name: String){
        this.name = name
    }

    public fun getName(): String{
        return this.name
    }

    public fun setImage(image: Int){
        this.image = image
    }

    public fun getImage(): Int{
        return this.image
    }

    public fun setIsCompleted(isCompleted: Boolean){
        this.isCompleted = isCompleted
    }

    public fun getIsCompleted(): Boolean{
        return this.isCompleted
    }

    public fun setIsSelected(isSelected: Boolean){
        this.isSelected = isSelected
    }

    public fun getIsSelected(): Boolean{
        return this.isSelected
    }
}