Rails.application.routes.draw do
  resources :real_time_displays
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  root 'real_time_displays#index'
end
