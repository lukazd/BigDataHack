class CreateRealTimeDisplays < ActiveRecord::Migration[5.0]
  def change
    create_table :real_time_displays do |t|

      t.timestamps
    end
  end
end
