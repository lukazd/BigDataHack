require 'test_helper'

class RealTimeDisplaysControllerTest < ActionDispatch::IntegrationTest
  setup do
    @real_time_display = real_time_displays(:one)
  end

  test "should get index" do
    get real_time_displays_url
    assert_response :success
  end

  test "should get new" do
    get new_real_time_display_url
    assert_response :success
  end

  test "should create real_time_display" do
    assert_difference('RealTimeDisplay.count') do
      post real_time_displays_url, params: { real_time_display: {  } }
    end

    assert_redirected_to real_time_display_url(RealTimeDisplay.last)
  end

  test "should show real_time_display" do
    get real_time_display_url(@real_time_display)
    assert_response :success
  end

  test "should get edit" do
    get edit_real_time_display_url(@real_time_display)
    assert_response :success
  end

  test "should update real_time_display" do
    patch real_time_display_url(@real_time_display), params: { real_time_display: {  } }
    assert_redirected_to real_time_display_url(@real_time_display)
  end

  test "should destroy real_time_display" do
    assert_difference('RealTimeDisplay.count', -1) do
      delete real_time_display_url(@real_time_display)
    end

    assert_redirected_to real_time_displays_url
  end
end
